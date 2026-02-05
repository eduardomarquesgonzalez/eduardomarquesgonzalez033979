package br.gov.mt.seplag.backend.dto.response;

import lombok.Builder;

@Builder
public record ArtistResponseDTO(
        Long id,
        String name,
        Integer albumCount
) {
}