package com.orchestra.api.entity;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "generated_test_data")

public class GeneratedTestDataEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "test_case_id")
    private GeneratedTestCaseEntity testCase;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private ProcessStepEntity step;

    @Column(columnDefinition = "jsonb")
    private String requestPayload;

    @Column(columnDefinition = "jsonb")
    private String responseExpected;

    public GeneratedTestDataEntity() {}

    public GeneratedTestDataEntity(UUID id,
                                   GeneratedTestCaseEntity testCase,
                                   ProcessStepEntity step,
                                   String requestPayload,
                                   String responseExpected) {
        this.id = id;
        this.testCase = testCase;
        this.step = step;
        this.requestPayload = requestPayload;
        this.responseExpected = responseExpected;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public GeneratedTestCaseEntity getTestCase() { return testCase; }
    public void setTestCase(GeneratedTestCaseEntity testCase) { this.testCase = testCase; }

    public ProcessStepEntity getStep() { return step; }
    public void setStep(ProcessStepEntity step) { this.step = step; }

    public String getRequestPayload() { return requestPayload; }
    public void setRequestPayload(String requestPayload) { this.requestPayload = requestPayload; }

    public String getResponseExpected() { return responseExpected; }
    public void setResponseExpected(String responseExpected) { this.responseExpected = responseExpected; }
}
