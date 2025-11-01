package com.orchestra.api.repository.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orchestra.api.entity.GeneratedTestCaseEntity;

public interface GeneratedTestCaseRepository extends JpaRepository<GeneratedTestCaseEntity, UUID>{

}
