package com.orchestra.api.dto.response;

import java.util.List;
import java.util.UUID;

public class BpmnDiagramResponse {
    private UUID diagramId;
    private String name;
    private String type;
    private String status;
    private List<StepResponse> steps;
    private List<TransitionResponse> transitions;

    public BpmnDiagramResponse() {}

    public BpmnDiagramResponse(UUID diagramId, String name, String type, String status,
                               List<StepResponse> steps, List<TransitionResponse> transitions) {
        this.diagramId = diagramId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.steps = steps;
        this.transitions = transitions;
    }

    public UUID getDiagramId() { return diagramId; }
    public void setDiagramId(UUID diagramId) { this.diagramId = diagramId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<StepResponse> getSteps() { return steps; }
    public void setSteps(List<StepResponse> steps) { this.steps = steps; }

    public List<TransitionResponse> getTransitions() { return transitions; }
    public void setTransitions(List<TransitionResponse> transitions) { this.transitions = transitions; }
}
