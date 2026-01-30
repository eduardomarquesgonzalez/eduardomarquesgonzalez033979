package br.gov.mt.seplag.backend.dto;

import lombok.Data;

@Data
public class ImageAlbumDTO {
    private Long id;
    private String fileName;
    private String imageUrl;
}
