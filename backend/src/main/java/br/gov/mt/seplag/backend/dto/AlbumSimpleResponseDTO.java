package br.gov.mt.seplag.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "DTO simplificado de álbum para listagens")
@Builder
public record AlbumSimpleResponseDTO(

        @Schema(description = "ID do álbum", example = "1")
        Long id,

        @Schema(description = "Título do álbum", example = "Abbey Road")
        String title,

        @Schema(description = "URL da capa do álbum (pré-assinada)")
        String coverUrl
) {
}