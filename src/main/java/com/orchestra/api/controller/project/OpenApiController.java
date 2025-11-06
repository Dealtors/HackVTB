package com.orchestra.api.controller.project;

import com.orchestra.api.dto.response.OpenApiUploadResponse;
import com.orchestra.api.service.project.OpenApiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/openapi")
public class OpenApiController {

    private final OpenApiService service;

    public OpenApiController(OpenApiService service) { this.service = service; }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "name", required = false) String name) {
        try {
            return ResponseEntity.ok(service.upload(file, name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to parse OpenAPI: " + e.getMessage());
        }
    }
}
