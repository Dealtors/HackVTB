package com.orchestra.api.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "generated_test_case")
public class GeneratedTestCaseEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "diagram_id")
    private ProcessDiagramEntity diagram;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String stepsJson;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedTestDataEntity> testData;

    public GeneratedTestCaseEntity() {}

    public GeneratedTestCaseEntity(UUID id,
                                   ProcessDiagramEntity diagram,
                                   String name,
                                   String stepsJson,
                                   LocalDateTime createdAt,
                                   List<GeneratedTestDataEntity> testData) {
        this.id = id;
        this.diagram = diagram;
        this.name = name;
        this.stepsJson = stepsJson;
        this.createdAt = createdAt;
        this.testData = testData;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ProcessDiagramEntity getDiagram() { return diagram; }
    public void setDiagram(ProcessDiagramEntity diagram) { this.diagram = diagram; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStepsJson() { return stepsJson; }
    public void setStepsJson(String stepsJson) { this.stepsJson = stepsJson; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<GeneratedTestDataEntity> getTestData() { return testData; }
    public void setTestData(List<GeneratedTestDataEntity> testData) { this.testData = testData; }
}
