package com.orchestra.api.controller.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orchestra.api.dto.response.ApiResponse;

@RestController
@RequestMapping("upload")
public class BpmnController {

    @RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("Сервер работает");
    }
}
}
