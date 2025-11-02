package com.orchestra.api.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import com.orchestra.api.entity.StepApiBindingEntity;

public interface StepApiBindingRepository extends JpaRepository<StepApiBindingEntity, UUID> {
}