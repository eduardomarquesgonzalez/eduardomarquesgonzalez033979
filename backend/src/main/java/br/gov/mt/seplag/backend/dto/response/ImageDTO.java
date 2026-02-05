package br.gov.mt.seplag.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Informações de uma imagem do álbum")
@Builder
public record ImageDTO(

        @Schema(description = "ID da imagem", example = "1")
        Long id,

        @Schema(description = "Nome do arquivo original", example = "abbey-road-cover.jpg")
        String fileName,

        @Schema(description = "URL pré-assinada (válida por 30min)")
        String url,

        @Schema(description = "Tipo MIME", example = "image/jpeg")
        String contentType,

        @Schema(description = "Indica se esta é a capa principal", example = "true")
        Boolean isPrimary
) {}