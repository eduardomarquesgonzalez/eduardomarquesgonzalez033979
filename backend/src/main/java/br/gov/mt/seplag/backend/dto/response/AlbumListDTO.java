package br.gov.mt.seplag.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

@Schema(description = "DTO simplificado para listagem de álbuns")
@Builder
public record AlbumListDTO(

        @Schema(description = "ID do álbum", example = "1")
        Long id,

        @Schema(description = "Título do álbum", example = "Abbey Road")
        String title,

        @Schema(description = "URL da capa do álbum")
        String coverUrl,

        @Schema(description = "Resumo dos artistas vinculados")
        Set<ArtistSummaryDTO> artists
) {

    @Builder
    public record ArtistSummaryDTO(
            Long id,
            String name
    ) {
    }
}