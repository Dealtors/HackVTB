package com.orchestra.api.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "process_step")
public class ProcessStepEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "diagram_id")
    private ProcessDiagramEntity diagram;

    private String stepId;
    private String name;
    private String actorFrom;
    private String actorTo;
    private String action;

    @Column(columnDefinition = "jsonb")
    private String nextSteps;

    @OneToMany(mappedBy = "fromStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessTransitionEntity> outgoing;

    @OneToMany(mappedBy = "toStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessTransitionEntity> incoming;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepApiBindingEntity> apiBindings;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedTestDataEntity> testData;

    public ProcessStepEntity() {}

    public ProcessStepEntity(UUID id, ProcessDiagramEntity diagram, String stepId,
                             String name, String actorFrom, String actorTo,
                             String action, String nextSteps,
                             List<ProcessTransitionEntity> outgoing,
                             List<ProcessTransitionEntity> incoming,
                             List<StepApiBindingEntity> apiBindings,
                             List<GeneratedTestDataEntity> testData) {
        this.id = id;
        this.diagram = diagram;
        this.stepId = stepId;
        this.name = name;
        this.actorFrom = actorFrom;
        this.actorTo = actorTo;
        this.action = action;
        this.nextSteps = nextSteps;
        this.outgoing = outgoing;
        this.incoming = incoming;
        this.apiBindings = apiBindings;
        this.testData = testData;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ProcessDiagramEntity getDiagram() { return diagram; }
    public void setDiagram(ProcessDiagramEntity diagram) { this.diagram = diagram; }

    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getActorFrom() { return actorFrom; }
    public void setActorFrom(String actorFrom) { this.actorFrom = actorFrom; }

    public String getActorTo() { return actorTo; }
    public void setActorTo(String actorTo) { this.actorTo = actorTo; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getNextSteps() { return nextSteps; }
    public void setNextSteps(String nextSteps) { this.nextSteps = nextSteps; }

    public List<ProcessTransitionEntity> getOutgoing() { return outgoing; }
    public void setOutgoing(List<ProcessTransitionEntity> outgoing) { this.outgoing = outgoing; }

    public List<ProcessTransitionEntity> getIncoming() { return incoming; }
    public void setIncoming(List<ProcessTransitionEntity> incoming) { this.incoming = incoming; }

    public List<StepApiBindingEntity> getApiBindings() { return apiBindings; }
    public void setApiBindings(List<StepApiBindingEntity> apiBindings) { this.apiBindings = apiBindings; }

    public List<GeneratedTestDataEntity> getTestData() { return testData; }
    public void setTestData(List<GeneratedTestDataEntity> testData) { this.testData = testData; }
}