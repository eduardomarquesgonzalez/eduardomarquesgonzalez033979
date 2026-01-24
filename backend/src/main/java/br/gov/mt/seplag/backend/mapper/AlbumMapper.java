package br.gov.mt.seplag.backend.mapper;

import br.gov.mt.seplag.backend.dto.request.AlbumRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumMapper implements BaseMapper<Album, AlbumRequestDTO, AlbumResponseDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Album toEntity(AlbumRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Album.builder()
                .title(requestDTO.getTitle())
                .releaseDate(requestDTO.getReleaseDate())
                .numberOfTracks(requestDTO.getNumberOfTracks())
                .description(requestDTO.getDescription())
                .build();
    }

    public Album toEntity(AlbumRequestDTO requestDTO, Artist artist) {
        if (requestDTO == null) {
            return null;
        }

        Album album = toEntity(requestDTO);
        album.setArtist(artist);

        return album;
    }

    @Override
    public AlbumResponseDTO toResponseDTO(Album entity) {
        if (entity == null) {
            return null;
        }
        AlbumResponseDTO dto = modelMapper.map(entity, AlbumResponseDTO.class);

        if (entity.getArtist() != null) {
            dto.setArtistId(entity.getArtist().getId());
            dto.setArtistName(entity.getArtist().getName());
        }
        return dto;
    }

    public AlbumResponseDTO toResponseDTOSimple(Album entity) {
        if (entity == null) {
            return null;
        }

        return AlbumResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .releaseDate(entity.getReleaseDate())
                .numberOfTracks(entity.getNumberOfTracks())
                .description(entity.getDescription())
                .artistId(entity.getArtist() != null ? entity.getArtist().getId() : null)
                .artistName(entity.getArtist() != null ? entity.getArtist().getName() : null)
                .build();
    }

    @Override
    public void updateEntityFromDTO(AlbumRequestDTO requestDTO, Album entity) {
        if (requestDTO == null || entity == null) {
            return;
        }

        if (requestDTO.getTitle() != null) {
            entity.setTitle(requestDTO.getTitle());
        }

        if (requestDTO.getReleaseDate() != null) {
            entity.setReleaseDate(requestDTO.getReleaseDate());
        }

        if (requestDTO.getNumberOfTracks() != null) {
            entity.setNumberOfTracks(requestDTO.getNumberOfTracks());
        }

        if (requestDTO.getDescription() != null) {
            entity.setDescription(requestDTO.getDescription());
        }
    }


    public void updateEntityFromDTO(AlbumRequestDTO requestDTO, Album entity, Artist artist) {
        updateEntityFromDTO(requestDTO, entity);

        if (artist != null) {
            entity.setArtist(artist);
        }
    }
}