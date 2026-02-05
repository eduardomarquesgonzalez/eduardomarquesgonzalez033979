package br.gov.mt.seplag.backend.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
        String fullName,
        String username,
        String role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastLogin
) {
}