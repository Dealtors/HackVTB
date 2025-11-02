package com.orchestra.api.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchestra.api.dto.response.SequenceDiagramResponse;
import com.orchestra.api.entity.ProcessDiagramEntity;
import com.orchestra.api.entity.ProcessStepEntity;
import com.orchestra.api.entity.ProcessTransitionEntity;
import com.orchestra.api.entity.SequenceDiagramRawEntity;
import com.orchestra.api.parser.SequenceParser;
import com.orchestra.api.repository.entity.ProcessDiagramRepository;
import com.orchestra.api.repository.entity.ProcessStepRepository;
import com.orchestra.api.repository.entity.ProcessTransitionRepository;
import com.orchestra.api.repository.entity.SequenceDiagramRawRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SequenceService {

    private final SequenceDiagramRawRepository rawRepo;
    private final ProcessDiagramRepository diagramRepo;
    private final ProcessStepRepository stepRepo;
    private final ProcessTransitionRepository transitionRepo;
    private final SequenceParser parser;

    public SequenceService(SequenceDiagramRawRepository rawRepo,
                           ProcessDiagramRepository diagramRepo,
                           ProcessStepRepository stepRepo,
                           ProcessTransitionRepository transitionRepo,
                           SequenceParser parser) {
        this.rawRepo = rawRepo;
        this.diagramRepo = diagramRepo;
        this.stepRepo = stepRepo;
        this.transitionRepo = transitionRepo;
        this.parser = parser;
    }

    @Transactional
    public SequenceDiagramResponse uploadSequence(String name, String format, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Empty file");

        // 1) Save raw
        UUID rawId = UUID.randomUUID();
        SequenceDiagramRawEntity raw = new SequenceDiagramRawEntity();
        raw.setId(rawId);
        raw.setName(name);
        raw.setFormat(format);
        raw.setRawContent(new String(file.getBytes(), StandardCharsets.UTF_8));
        raw.setCreatedAt(LocalDateTime.now());
        rawRepo.save(raw);

        // 2) Parse
        SequenceParser.ParsedSequence parsed = parser.parse(raw.getRawContent());

        // 3) Persist into unified process_* model (type=SEQUENCE)
        UUID dId = UUID.randomUUID();
        ProcessDiagramEntity d = new ProcessDiagramEntity();
        d.setId(dId);
        d.setName(name != null ? name : "Sequence");
        d.setType("SEQUENCE");
        d.setStatus("READY");
        d.setCreatedAt(LocalDateTime.now());
        d.setUpdatedAt(LocalDateTime.now());
        d.setSteps(new ArrayList<>());
        d.setTransitions(new ArrayList<>());
        diagramRepo.save(d);

        Map<String, ProcessStepEntity> byStepId = new LinkedHashMap<>();
        for (SequenceDiagramResponse.Step s : parsed.steps()) {
            ProcessStepEntity e = new ProcessStepEntity();
            e.setId(UUID.randomUUID());
            e.setDiagram(d);
            e.setStepId(s.stepId);
            e.setName(s.name);
            e.setActorFrom(s.from);
            e.setActorTo(s.to);
            e.setAction(s.action);
            e.setNextSteps(new ObjectMapper().writeValueAsString(s.next)); // jsonb text
            stepRepo.save(e);
            byStepId.put(s.stepId, e);
            d.getSteps().add(e);
        }
        for (SequenceDiagramResponse.Step s : parsed.steps()) {
            for (String nxt : s.next) {
                ProcessTransitionEntity t = new ProcessTransitionEntity();
                t.setId(UUID.randomUUID());
                t.setDiagram(d);
                t.setFromStep(byStepId.get(s.stepId));
                t.setToStep(byStepId.get(nxt));
                transitionRepo.save(t);
                d.getTransitions().add(t);
            }
        }

        // 4) Build response
        SequenceDiagramResponse resp = new SequenceDiagramResponse();
        resp.setId(dId);
        resp.setName(d.getName());
        resp.setType("SEQUENCE");
        resp.setActors(parsed.actors());
        resp.setSteps(parsed.steps());
        return resp;
    }
}

