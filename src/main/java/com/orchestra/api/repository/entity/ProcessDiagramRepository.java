package com.orchestra.api.repository.entity;
import com.orchestra.api.entity.ProcessDiagramEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProcessDiagramRepository extends JpaRepository<ProcessDiagramEntity, UUID> {
}