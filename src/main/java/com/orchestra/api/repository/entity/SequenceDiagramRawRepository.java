// src/main/java/com/orchestra/api/repository/entity/SequenceDiagramRawRepository.java
package com.orchestra.api.repository.entity;

import com.orchestra.api.entity.SequenceDiagramRawEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SequenceDiagramRawRepository extends JpaRepository<SequenceDiagramRawEntity, UUID> {}
