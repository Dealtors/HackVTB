package com.orchestra.api.repository.entity;

import com.orchestra.api.entity.ProcessStepEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ProcessStepRepository extends JpaRepository<ProcessStepEntity, UUID> {
}
