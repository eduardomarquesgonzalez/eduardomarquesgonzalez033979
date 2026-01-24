package br.gov.mt.seplag.backend.dto.response;

import br.gov.mt.seplag.backend.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistResponseDTO {

    private Long id;
    private String name;
    private String type;

    @Builder.Default
    private List<AlbumResponseDTO> albums = new ArrayList<>();

}