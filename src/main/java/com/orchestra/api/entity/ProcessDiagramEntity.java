package com.orchestra.api.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "process_diagram")
public class ProcessDiagramEntity {

    @Id
    private UUID id;

    private String name;

    private String type; // BPMN or SEQUENCE

    private String status; // PENDING / PARSED / READY / FAILED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessStepEntity> steps;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessTransitionEntity> transitions;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedTestCaseEntity> testCases;

    public ProcessDiagramEntity() {}

    public ProcessDiagramEntity(UUID id, String name, String type, String status,
                                LocalDateTime createdAt, LocalDateTime updatedAt,
                                List<ProcessStepEntity> steps,
                                List<ProcessTransitionEntity> transitions,
                                List<GeneratedTestCaseEntity> testCases) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.steps = steps;
        this.transitions = transitions;
        this.testCases = testCases;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ProcessStepEntity> getSteps() { return steps; }
    public void setSteps(List<ProcessStepEntity> steps) { this.steps = steps; }

    public List<ProcessTransitionEntity> getTransitions() { return transitions; }
    public void setTransitions(List<ProcessTransitionEntity> transitions) { this.transitions = transitions; }

    public List<GeneratedTestCaseEntity> getTestCases() { return testCases; }
    public void setTestCases(List<GeneratedTestCaseEntity> testCases) { this.testCases = testCases; }
}