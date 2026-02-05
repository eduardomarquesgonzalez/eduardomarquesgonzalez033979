package br.gov.mt.seplag.backend.dto.response;

import br.gov.mt.seplag.backend.dto.AlbumSimpleResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "Detalhes completos do artista incluindo álbuns")
@Builder
public record ArtistDetailResponseDTO(

        @Schema(description = "ID do artista", example = "1")
        Long id,

        @Schema(description = "Nome do artista", example = "The Beatles")
        String name,

        @Schema(description = "Lista de álbuns do artista")
        List<AlbumSimpleResponseDTO> albums
) {
}