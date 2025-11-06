package com.orchestra.api.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchestra.api.dto.response.OpenApiUploadResponse;
import com.orchestra.api.entity.OpenApiSpecEntity;
import com.orchestra.api.mapper.OpenApiMapper;
import com.orchestra.api.repository.entity.OpenApiSpecRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.parser.OpenAPIV3Parser;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OpenApiService {

    private final OpenApiSpecRepository specRepo;
    private final OpenApiMapper mapper;
    private final ObjectMapper json;

    public OpenApiService(OpenApiSpecRepository specRepo, OpenApiMapper mapper) {
        this.specRepo = specRepo;
        this.mapper = mapper;
        this.json = new ObjectMapper();
    }

    @Transactional
    public OpenApiUploadResponse upload(MultipartFile file, String name) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }

        // 1) Читаем исходный текст (это может быть и YAML, и JSON)
        final String txt;
        try {
            txt = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot read file");
        }

        // 2) Парсим как OpenAPI (поддерживает YAML/JSON)
        ParseOptions opts = new ParseOptions();
        opts.setResolve(false);
        SwaggerParseResult pr = new OpenAPIV3Parser().readContents(txt, null, opts);
        OpenAPI openAPI = pr.getOpenAPI();
        if (openAPI == null || openAPI.getPaths() == null || openAPI.getPaths().isEmpty()) {
            String msg = (pr.getMessages() != null && !pr.getMessages().isEmpty())
                    ? String.join("; ", pr.getMessages())
                    : "OpenAPI parse failed";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
        }

        // 3) Нормализуем в JSON, чтобы класть в JSONB независимо от входного формата
        final String normalizedJson;
        try {
            normalizedJson = json.writeValueAsString(openAPI);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot serialize OpenAPI to JSON");
        }

        // 4) Сохраняем сырьё (assigned UUID)
        UUID id = UUID.randomUUID();
        OpenApiSpecEntity e = new OpenApiSpecEntity();
        e.setId(id);
        e.setName(name != null ? name : file.getOriginalFilename());
        e.setFileName(file.getOriginalFilename());
        e.setSpecJson(normalizedJson); // всегда валидный JSON для JSONB
        e.setCreatedAt(LocalDateTime.now());
        specRepo.save(e);

        // 5) Формируем ответ
        return mapper.toResponse(id, openAPI);
    }
}
