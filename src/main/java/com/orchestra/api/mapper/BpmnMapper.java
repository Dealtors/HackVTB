package com.orchestra.api.mapper;

import com.orchestra.api.entity.ProcessDiagramEntity;
import com.orchestra.api.model.core.BpmnElement;
import com.orchestra.api.model.project.BpmnDiagram;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.orchestra.api.entity.ProcessStepEntity;
import com.orchestra.api.entity.ProcessTransitionEntity;

public class BpmnMapper {

    public ProcessDiagramEntity toEntity(BpmnDiagram model) {
        UUID id = model.getId() != null ? model.getId() : UUID.randomUUID();

        ProcessDiagramEntity diagramEntity = new ProcessDiagramEntity(
                id,
                model.getName(),
                model.getType(),
                "PARSED",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );


        Map<String, ProcessStepEntity> stepEntityMap = new HashMap<>();
            for (BpmnElement step : model.getSteps()) {
                ProcessStepEntity stepEntity = new ProcessStepEntity(
                        UUID.randomUUID(),
                        diagramEntity,
                        step.getStepId(),
                        step.getName(),
                        step.getActorFrom(),
                        step.getActorTo(),
                        step.getAction(),
                        serializeNextSteps(step.getNextSteps()),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
                stepEntityMap.put(step.getStepId(), stepEntity);
                diagramEntity.getSteps().add(stepEntity);
            }

            // 2. Map transitions
            for (ProcessStepEntity fromStep : stepEntityMap.values()) {
                List<String> nextIds = deserializeNextSteps(fromStep.getNextSteps());
                for (String nextId : nextIds) {
                    ProcessStepEntity toStep = stepEntityMap.get(nextId);
                    if (toStep != null) {
                        ProcessTransitionEntity transition = new ProcessTransitionEntity(
                                UUID.randomUUID(),
                                diagramEntity,
                                fromStep,
                                toStep
                        );
                        diagramEntity.getTransitions().add(transition);
                    }
                }
            }

            return diagramEntity;
        }

    private String serializeNextSteps(List<String> nextSteps) {
        return nextSteps == null ? "[]" : String.join(",", nextSteps);
    }

    private List<String> deserializeNextSteps(String nextSteps) {
        return nextSteps == null || nextSteps.isEmpty()
                ? List.of()
                : Arrays.asList(nextSteps.split(","));
    }

}
