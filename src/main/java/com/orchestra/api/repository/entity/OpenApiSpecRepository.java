package com.orchestra.api.repository.entity;

import com.orchestra.api.entity.OpenApiSpecEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OpenApiSpecRepository extends JpaRepository<OpenApiSpecEntity, UUID> {}

