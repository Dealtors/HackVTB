package com.orchestra.api.repository.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orchestra.api.entity.ProcessTransitionEntity;

public interface ProcessTransitionRepository extends JpaRepository<ProcessTransitionEntity, UUID>{

}
