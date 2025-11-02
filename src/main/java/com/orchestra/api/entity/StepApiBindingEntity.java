package com.orchestra.api.entity;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "step_api_binding")
public class StepApiBindingEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private ProcessStepEntity step;

    private String httpMethod;

    private String apiPath;

    private Double confidenceScore;

    public StepApiBindingEntity() {}

    public StepApiBindingEntity(UUID id,
                                ProcessStepEntity step,
                                String httpMethod,
                                String apiPath,
                                Double confidenceScore) {
        this.id = id;
        this.step = step;
        this.httpMethod = httpMethod;
        this.apiPath = apiPath;
        this.confidenceScore = confidenceScore;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ProcessStepEntity getStep() { return step; }
    public void setStep(ProcessStepEntity step) { this.step = step; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public String getApiPath() { return apiPath; }
    public void setApiPath(String apiPath) { this.apiPath = apiPath; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
}
