package com.orchestra.api.mapper;

import com.orchestra.api.dto.response.OpenApiUploadResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OpenApiMapper {

    public OpenApiUploadResponse toResponse(UUID id, OpenAPI openAPI) {
        if (openAPI == null || openAPI.getPaths() == null || openAPI.getPaths().isEmpty()) {
            throw new IllegalArgumentException("No paths found in OpenAPI spec");
        }

        // Группируем операции по пути, чтобы не перетирать методы одного path
        Map<String, Map<String, OpenApiUploadResponse.Item>> byPath = new LinkedHashMap<>();

        openAPI.getPaths().forEach((path, pathItem) -> {
            if (pathItem == null || pathItem.readOperationsMap() == null) return;
            Map<String, OpenApiUploadResponse.Item> methods = byPath.computeIfAbsent(path, k -> new LinkedHashMap<>());

            pathItem.readOperationsMap().forEach((method, op) -> {
                if (op == null) return;
                OpenApiUploadResponse.Item it = new OpenApiUploadResponse.Item();
                it.method = method.name();
                it.requestSchema = extractRequestSchema(op, openAPI);
                it.responses = extractResponses(op);
                methods.put(method.name(), it);
            });
        });

        // Если твой DTO пока плоский (Map<String, Item>), оставим backward-compatible фолбэк:
        // выбираем приоритетный метод per path (POST > PUT > GET > DELETE > прочие)
        List<String> priority = List.of("POST", "PUT", "GET", "DELETE", "PATCH", "OPTIONS", "HEAD");
        Map<String, OpenApiUploadResponse.Item> flat = new LinkedHashMap<>();
        byPath.forEach((path, methods) -> {
            OpenApiUploadResponse.Item chosen = null;
            for (String m : priority) { if (methods.containsKey(m)) { chosen = methods.get(m); break; } }
            if (chosen == null && !methods.isEmpty()) { chosen = methods.values().iterator().next(); }
            if (chosen != null) flat.put(path, chosen);
        });

        OpenApiUploadResponse resp = new OpenApiUploadResponse();
        resp.setId(id);
        // Если уже изменили контракт — можно положить byPath; иначе кладём flat
        resp.setApiList(flat);
        return resp;
    }

    private Map<String, Object> extractRequestSchema(Operation op, OpenAPI api) {
        Map<String, Object> schema = new LinkedHashMap<>();
        if (op.getRequestBody() == null || op.getRequestBody().getContent() == null) return schema;

        // Берём application/json, иначе первый доступный content
        Schema<?> s = null;
        if (op.getRequestBody().getContent().get("application/json") != null) {
            s = op.getRequestBody().getContent().get("application/json").getSchema();
        }
        if (s == null) {
            var first = op.getRequestBody().getContent().values().stream().findFirst();
            if (first.isPresent()) s = first.get().getSchema();
        }
        if (s != null) schema.putAll(flattenSchema(s, api));
        return schema;
    }

    private Map<String, String> extractResponses(Operation op) {
        Map<String, String> res = new LinkedHashMap<>();
        if (op.getResponses() == null) return res;
        op.getResponses().forEach((code, r) -> res.put(code, r != null && r.getDescription() != null ? r.getDescription() : ""));
        return res;
    }

    // Упрощённое «безопасное» разворачивание
    private Map<String, Object> flattenSchema(Schema<?> s, OpenAPI api) {
        Map<String, Object> out = new LinkedHashMap<>();
        if (s == null) return out;

        // $ref: пишем ref и, если удаётся, тип целевой схемы
        if (s.get$ref() != null) {
            out.put("$ref", s.get$ref());
            String name = s.get$ref().substring(s.get$ref().lastIndexOf('/') + 1);
            if (api != null && api.getComponents() != null && api.getComponents().getSchemas() != null) {
                Schema<?> target = api.getComponents().getSchemas().get(name);
                if (target != null && target.getType() != null) out.put("type", target.getType());
            }
            return out;
        }

        // Массив
        if (s instanceof io.swagger.v3.oas.models.media.ArraySchema arr) {
            Map<String, Object> items = flattenSchema(arr.getItems(), api);
            out.put("type", "array");
            if (!items.isEmpty()) out.put("items", items);
            return out;
        }

        // Объект с properties
        if (s.getProperties() != null && !s.getProperties().isEmpty()) {
            s.getProperties().forEach((k, v) -> {
                Map<String, Object> sub = flattenSchema((Schema<?>) v, api);
                // если ничего не извлекли, хотя бы type/null
                if (sub.isEmpty() && v.getType() != null) sub.put("type", v.getType());
                out.put(k, sub.isEmpty() ? (v.getType() != null ? v.getType() : "object") : sub);
            });
            return out;
        }

        // oneOf/anyOf/allOf (3.0/3.1)
        if (s instanceof io.swagger.v3.oas.models.media.ComposedSchema comp) {
            if (comp.getOneOf() != null && !comp.getOneOf().isEmpty())
                out.put("oneOf", comp.getOneOf().stream().map(x -> flattenSchema(x, api)).toList());
            if (comp.getAnyOf() != null && !comp.getAnyOf().isEmpty())
                out.put("anyOf", comp.getAnyOf().stream().map(x -> flattenSchema(x, api)).toList());
            if (comp.getAllOf() != null && !comp.getAllOf().isEmpty())
                out.put("allOf", comp.getAllOf().stream().map(x -> flattenSchema(x, api)).toList());
            if (s.getType() != null) out.putIfAbsent("type", s.getType());
            return out;
        }

        // Базовый случай
        if (s.getType() != null) out.put("type", s.getType());
        return out;
    }
}
