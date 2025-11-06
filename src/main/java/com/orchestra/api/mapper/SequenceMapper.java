package com.orchestra.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchestra.api.dto.response.SequenceDiagramResponse;
import com.orchestra.api.entity.ProcessDiagramEntity;
import com.orchestra.api.entity.ProcessStepEntity;
import com.orchestra.api.entity.ProcessTransitionEntity;
import com.orchestra.api.model.core.BpmnElement;
import com.orchestra.api.model.project.SequenceDiagram;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class SequenceMapper {

    private final ObjectMapper om = new ObjectMapper();

    public ProcessDiagramEntity toEntity(SequenceDiagram model) {
        UUID did = model.getId() != null ? model.getId() : UUID.randomUUID();
        ProcessDiagramEntity diagram = new ProcessDiagramEntity(
                did,
                model.getName(),
                "SEQUENCE",
                "PARSED",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Map<String, ProcessStepEntity> byStepId = new LinkedHashMap<>();
        for (BpmnElement s : model.getSteps()) {
            ProcessStepEntity e = new ProcessStepEntity(
                    UUID.randomUUID(),
                    diagram,
                    s.getStepId(),
                    s.getName(),
                    s.getActorFrom(),
                    s.getActorTo(),
                    s.getAction(),
                    serializeNextSteps(s.getNextSteps()),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            byStepId.put(s.getStepId(), e);
            diagram.getSteps().add(e);
        }
        for (ProcessStepEntity from : byStepId.values()) {
            for (String nid : deserializeNextSteps(from.getNextSteps())) {
                ProcessStepEntity to = byStepId.get(nid);
                if (to != null) {
                    ProcessTransitionEntity t = new ProcessTransitionEntity(
                            UUID.randomUUID(), diagram, from, to
                    );
                    diagram.getTransitions().add(t);
                }
            }
        }
        return diagram;
    }

    public SequenceDiagramResponse toResponse(ProcessDiagramEntity diagram, List<String> actors) {
        SequenceDiagramResponse resp = new SequenceDiagramResponse();
        resp.setId(diagram.getId());
        resp.setName(diagram.getName());
        resp.setType(diagram.getType());
        resp.setActors(actors);

        List<SequenceDiagramResponse.Step> steps = new ArrayList<>();
        for (ProcessStepEntity s : diagram.getSteps()) {
            SequenceDiagramResponse.Step st = new SequenceDiagramResponse.Step();
            st.stepId = s.getStepId();
            st.name   = s.getName();
            st.from   = s.getActorFrom();
            st.to     = s.getActorTo();
            st.action = s.getAction();
            st.next   = deserializeNextSteps(s.getNextSteps());
            steps.add(st);
        }
        resp.setSteps(steps);
        return resp;
    }

    private String serializeNextSteps(List<String> next) {
        if (next == null || next.isEmpty()) return "[]";
        try { return om.writeValueAsString(next); } catch (JsonProcessingException e) { return "[]"; }
    }

    private List<String> deserializeNextSteps(String next) {
        if (next == null || next.isEmpty() || "[]".equals(next)) return List.of();
        try { return om.readValue(next, new TypeReference<List<String>>(){}); }
        catch (Exception e) { return List.of(); }
    }
}
