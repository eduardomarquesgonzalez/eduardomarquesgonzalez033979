package br.gov.mt.seplag.backend.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbumSimpleResponseDTO {

    private Long id;
    private String title;
}
