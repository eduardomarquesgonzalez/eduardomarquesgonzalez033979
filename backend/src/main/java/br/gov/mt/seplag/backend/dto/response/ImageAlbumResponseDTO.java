package br.gov.mt.seplag.backend.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ImageAlbumResponseDTO(
        Long id,
        String objectKey,
        String contentType,
        String imageUrl,
        Boolean isPrimary,
        LocalDateTime createdAt
) {
}