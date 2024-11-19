package com.codequest.backend.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class CommentListConverter implements AttributeConverter<List<Comment>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Comment> comments) {
        try {
            return objectMapper.writeValueAsString(comments);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting comments to JSON", e);
        }
    }

    @Override
    public List<Comment> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>(); // 기본값 반환
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Comment>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to comments", e);
        }
    }
}
