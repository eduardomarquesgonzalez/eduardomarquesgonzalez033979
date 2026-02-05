package br.gov.mt.seplag.backend.dto.request;

import jakarta.validation.constraints.NotBlank;


public record ArtistRequestDTO(

        @NotBlank @NotBlank String name
) {
    public static ArtistRequestDTO of(String name) {
        return new ArtistRequestDTO(name);
    }

}
