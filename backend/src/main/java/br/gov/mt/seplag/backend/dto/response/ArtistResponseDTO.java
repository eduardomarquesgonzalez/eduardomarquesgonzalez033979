package br.gov.mt.seplag.backend.dto.response;

import br.gov.mt.seplag.backend.dto.AlbumSimpleResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArtistResponseDTO {

    private Long id;
    private String name;
    private List<AlbumSimpleResponseDTO> albums;

}
