package br.gov.mt.seplag.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * DTO com detalhes completos do álbum.
 * Inclui lista de artistas e todas as imagens do álbum.
 */
@Schema(description = "Detalhes completos do álbum incluindo artistas e imagens")
@Builder
public record AlbumDetailResponseDTO(

        @Schema(description = "ID do álbum", example = "1")
        Long id,

        @Schema(description = "Título do álbum", example = "Abbey Road")
        String title,

        @Schema(description = "URL da capa principal (pré-assinada por 30min)")
        String coverUrl,

        @Schema(description = "Lista de artistas do álbum")
        List<ArtistSimpleDTO> artists,

        @Schema(description = "Todas as imagens do álbum (requisito g)")
        List<ImageDTO> images
) {
}