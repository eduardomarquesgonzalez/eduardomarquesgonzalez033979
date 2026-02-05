package br.gov.mt.seplag.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "Dados do álbum para listagem")
@Builder
public record AlbumResponseDTO(

        @Schema(description = "ID do álbum", example = "1", required = true)
        Long id,

        @Schema(description = "Título do álbum", example = "Abbey Road", required = true)
        String title,

        @Schema(
                description = "URL da capa principal (pré-assinada, válida por 30min)",
                example = "http://localhost:9000/music-catalog/albums/1/cover.jpg?X-Amz-..."
        )
        String coverUrl,

        @Schema(
                description = "Nomes dos artistas (ordenados alfabeticamente)",
                example = "[\"George Martin\", \"The Beatles\"]",
                required = true
        )
        List<String> artistNames,

        @Schema(
                description = "Quantidade total de imagens do álbum",
                example = "3",
                required = true
        )
        Integer imageCount
) {}