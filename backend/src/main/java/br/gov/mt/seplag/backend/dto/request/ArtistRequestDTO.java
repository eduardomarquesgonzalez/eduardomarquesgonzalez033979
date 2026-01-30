package br.gov.mt.seplag.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArtistRequestDTO {

    @NotBlank
    @Size(max = 200)
    private String name;

}
