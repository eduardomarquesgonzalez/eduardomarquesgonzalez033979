package br.gov.mt.seplag.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponseDTO {

    private Long id;
    private String title;
    private LocalDate releaseDate;
    private Integer numberOfTracks;
    private String description;
    private Long artistId;
    private String artistName;
}