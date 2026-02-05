package br.gov.mt.seplag.backend.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record NotificationDTO(
        String type,
        Map<String, Object> data,
        LocalDateTime timestamp
) {
}