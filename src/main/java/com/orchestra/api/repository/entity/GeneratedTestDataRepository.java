package com.orchestra.api.repository.entity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orchestra.api.entity.GeneratedTestDataEntity;

public interface GeneratedTestDataRepository extends JpaRepository<GeneratedTestDataEntity, UUID>{

}
