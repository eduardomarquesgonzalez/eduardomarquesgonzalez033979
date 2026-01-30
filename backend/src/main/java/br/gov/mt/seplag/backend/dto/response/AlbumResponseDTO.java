package br.gov.mt.seplag.backend.dto.response;

import br.gov.mt.seplag.backend.dto.ImageAlbumDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponseDTO {

    private Long id;
    private String title;
    private Set<Long> artists;
    private Set<String> artistNames;

    private List<String> coverImageUrls;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ArtistSummaryDTO {
        private Long id;
        private String name;
    }
}