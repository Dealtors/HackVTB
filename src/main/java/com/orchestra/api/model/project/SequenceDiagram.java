package com.orchestra.api.model.project;

import com.orchestra.api.model.core.BpmnElement;
import java.util.List;
import java.util.UUID;

public class SequenceDiagram {
    private UUID id;
    private String name;
    private String type; // "SEQUENCE"
    private List<String> actors;
    private List<BpmnElement> steps;

    public SequenceDiagram() {}

    public SequenceDiagram(UUID id, String name, String type, List<String> actors, List<BpmnElement> steps) {
        this.id = id; this.name = name; this.type = type; this.actors = actors; this.steps = steps;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }
    public List<BpmnElement> getSteps() { return steps; }
    public void setSteps(List<BpmnElement> steps) { this.steps = steps; }
}
