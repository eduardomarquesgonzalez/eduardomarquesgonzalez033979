package br.gov.mt.seplag.backend.mapper;

import br.gov.mt.seplag.backend.dto.AlbumSimpleResponseDTO;
import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistDetailResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtistMapper {

    private final MinioStorageService storageService;

    public Artist toEntity(ArtistRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Artist.builder()
                .name(dto.name())
                .build();
    }

    public ArtistResponseDTO toResponse(Artist artist) {
        if (artist == null) {
            return null;
        }

        int albumCount = 0;
        try {
            albumCount = (artist.getAlbums() != null) ? artist.getAlbums().size() : 0;
        } catch (Exception e) {
            log.debug("Coleção de álbuns não inicializada para artista ID: {}", artist.getId());
        }

        return ArtistResponseDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .albumCount(albumCount)
                .build();
    }
    public ArtistDetailResponseDTO toDetailResponse(Artist artist) {
        if (artist == null) {
            return null;
        }

        log.debug("Convertendo artist ID: {} com {} álbuns",
                artist.getId(),
                artist.getAlbums() != null ? artist.getAlbums().size() : 0);

        return ArtistDetailResponseDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .albums(mapAlbumsToSimpleDTO(artist.getAlbums()))
                .build();
    }
    private List<AlbumSimpleResponseDTO> mapAlbumsToSimpleDTO(Set<Album> albums) {
        if (albums == null || albums.isEmpty()) {
            log.debug("Nenhum álbum para mapear");
            return Collections.emptyList();
        }

        log.debug("Mapeando {} álbuns para DTOs", albums.size());

        return albums.stream()
                .map(this::toAlbumSimpleDTO)
                .sorted(Comparator.comparing(
                        AlbumSimpleResponseDTO::title,
                        String.CASE_INSENSITIVE_ORDER
                ))
                .toList();
    }

    private AlbumSimpleResponseDTO toAlbumSimpleDTO(Album album) {
        if (album == null) {return null;}
        String coverUrl = null;
        if (album.getCoverUrl() != null && !album.getCoverUrl().isBlank()) {
            try {
                coverUrl = storageService.generatePresignedUrl(album.getCoverUrl(), 30);
            } catch (Exception e) {
                log.warn("Erro ao gerar URL pré-assinada para álbum ID: {}", album.getId());
            }
        }
        return AlbumSimpleResponseDTO.builder().id(album.getId()).title(album.getTitle()).coverUrl(coverUrl).build();
    }
}