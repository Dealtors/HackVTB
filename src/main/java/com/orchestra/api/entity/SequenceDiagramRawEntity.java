// src/main/java/com/orchestra/api/entity/SequenceDiagramRawEntity.java
package com.orchestra.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sequence_diagrams")
public class SequenceDiagramRawEntity {
    @Id
    private UUID id;

    private String name;

    private String format; // PLANTUML | MERMAID | CMMN

    @Lob
    @Column(name = "raw_content", nullable = false)
    private String rawContent;

    private LocalDateTime createdAt;

    public SequenceDiagramRawEntity() {}

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getRawContent() { return rawContent; }
    public void setRawContent(String rawContent) { this.rawContent = rawContent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
