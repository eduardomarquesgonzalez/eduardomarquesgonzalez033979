package br.gov.mt.seplag.backend.mapper;

import br.gov.mt.seplag.backend.dto.AlbumSimpleResponseDTO;
import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;

import java.util.List;

public class ArtistMapper {

    private ArtistMapper() {
    }

    public static Artist toEntity(ArtistRequestDTO dto) {
        return Artist.builder()
                .name(dto.getName())
                .build();
    }

    public static ArtistResponseDTO toResponse(Artist artist) {
        return ArtistResponseDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .albums(mapAlbums(artist.getAlbums()))
                .build();
    }

    private static List<AlbumSimpleResponseDTO> mapAlbums(
            Iterable<Album> albums) {

        return albums == null ? List.of() :
                ((List<Album>) albums).stream()
                        .map(album -> AlbumSimpleResponseDTO.builder()
                                .id(album.getId())
                                .title(album.getTitle())
                                .build())
                        .toList();
    }
}
