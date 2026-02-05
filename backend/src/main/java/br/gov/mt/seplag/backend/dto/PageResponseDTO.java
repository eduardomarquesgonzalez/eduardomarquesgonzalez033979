package br.gov.mt.seplag.backend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;
@Schema(description = "Resposta paginada")
@JsonPropertyOrder({"content", "currentPage", "totalPages", "totalElements"})
public record PageResponseDTO<T>(

        @Schema(description = "Conteúdo da página")
        List<T> content,

        @Schema(description = "Página atual (base 0)", example = "0")
        int currentPage,

        @Schema(description = "Total de páginas", example = "10")
        int totalPages,

        @Schema(description = "Total de elementos", example = "95")
        long totalElements
) {

    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}