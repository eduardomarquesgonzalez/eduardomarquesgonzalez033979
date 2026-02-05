package br.gov.mt.seplag.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Dados simplificados de um artista")
@Builder
public record ArtistSimpleDTO(

        @Schema(description = "ID do artista", example = "1")
        Long id,

        @Schema(description = "Nome do artista", example = "The Beatles")
        String name
) {
}