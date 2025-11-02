package com.orchestra.api.service.project;

import com.orchestra.api.entity.OpenApiSpecEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchestra.api.dto.response.OpenApiUploadResponse;
import com.orchestra.api.entity.OpenApiSpecEntity;
import com.orchestra.api.repository.entity.OpenApiSpecRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OpenApiService {

    private final OpenApiSpecRepository specRepo;

    public OpenApiService(OpenApiSpecRepository specRepo) { this.specRepo = specRepo; }

    @Transactional
    public OpenApiUploadResponse upload(MultipartFile file, String name) throws Exception {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Empty file");
        String txt = new String(file.getBytes(), StandardCharsets.UTF_8);

        // 1) Валидация JSON
        try { new ObjectMapper().readTree(txt); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON"); }

        // 2) Сохранить raw
        UUID id = UUID.randomUUID();
        OpenApiSpecEntity e = new OpenApiSpecEntity();
        e.setId(id);
        e.setName(name != null ? name : file.getOriginalFilename());
        e.setFileName(file.getOriginalFilename());
        e.setSpecJson(txt);
        e.setCreatedAt(LocalDateTime.now());
        specRepo.save(e);

        // 3) Парсинг OpenAPI
        OpenAPI openAPI = new OpenAPIV3Parser().readContents(txt, null, null).getOpenAPI();
        if (openAPI == null || openAPI.getPaths() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OpenAPI parse failed");
        }

        // 4) Сбор структуры
        Map<String, OpenApiUploadResponse.Item> map = new LinkedHashMap<>();
        openAPI.getPaths().forEach((path, item) -> {
            item.readOperationsMap().forEach((method, op) -> {
                OpenApiUploadResponse.Item it = new OpenApiUploadResponse.Item();
                it.method = method.name();
                it.requestSchema = extractRequestSchema(op);
                it.responses = extractResponses(op);
                map.put(path, it);
            });
        });

        OpenApiUploadResponse resp = new OpenApiUploadResponse();
        resp.setId(id);
        resp.setApiList(map);
        return resp;
    }

    private Map<String, Object> extractRequestSchema(Operation op) {
        Map<String, Object> schema = new LinkedHashMap<>();
        if (op.getRequestBody() != null && op.getRequestBody().getContent() != null) {
            op.getRequestBody().getContent().forEach((ct, media) -> {
                if (media.getSchema() != null) schema.putAll(flattenSchema(media.getSchema()));
            });
        }
        return schema;
    }

    private Map<String, String> extractResponses(Operation op) {
        Map<String, String> res = new LinkedHashMap<>();
        if (op.getResponses() != null) {
            op.getResponses().forEach((code, r) -> res.put(code, Optional.ofNullable(r.getDescription()).orElse("")));
        }
        return res;
    }

    private Map<String, Object> flattenSchema(Schema<?> s) {
        Map<String, Object> out = new LinkedHashMap<>();
        if (s.getProperties() != null) {
            s.getProperties().forEach((k, v) -> out.put(k, v.getType()));
        } else if (s.get$ref() != null) {
            out.put("$ref", s.get$ref());
        } else if (s.getType() != null) {
            out.put("type", s.getType());
        }
        return out;
    }
}

