package br.gov.mt.seplag.backend.dto;

import lombok.Builder;

@Builder
public record RegionalDTO(
        Long id,
        String nome
) {
}