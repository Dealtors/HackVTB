package com.orchestra.api.model.core;

import java.util.List;
import java.util.UUID;

public class BpmnElement {

    private UUID id;
    private String stepId;
    private String name;
    private String actorFrom;
    private String actorTo;
    private String action;
    private List<String> nextSteps;

    public BpmnElement() {}

    public BpmnElement(UUID id, String stepId, String name,
                       String actorFrom, String actorTo,
                       String action, List<String> nextSteps) {
        this.id = id;
        this.stepId = stepId;
        this.name = name;
        this.actorFrom = actorFrom;
        this.actorTo = actorTo;
        this.action = action;
        this.nextSteps = nextSteps;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActorFrom() {
        return actorFrom;
    }

    public void setActorFrom(String actorFrom) {
        this.actorFrom = actorFrom;
    }

    public String getActorTo() {
        return actorTo;
    }

    public void setActorTo(String actorTo) {
        this.actorTo = actorTo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(List<String> nextSteps) {
        this.nextSteps = nextSteps;
    }
}