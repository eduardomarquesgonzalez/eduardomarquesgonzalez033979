package br.gov.mt.seplag.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record AlbumRequestDTO(
        @NotBlank String title,
        @NotEmpty Set<Long> artistIds
) {
    public static AlbumRequestDTO of(String title, Long artistIds) {
        return new AlbumRequestDTO(title, Set.of(artistIds));
    }

    public static AlbumRequestDTO of(String title, Set<Long> artistIds) {
        return new AlbumRequestDTO(title, artistIds);
    }
}
