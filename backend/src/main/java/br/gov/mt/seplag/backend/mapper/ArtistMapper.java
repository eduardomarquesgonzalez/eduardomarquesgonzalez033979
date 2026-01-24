package br.gov.mt.seplag.backend.mapper;

import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ArtistMapper implements BaseMapper<Artist, ArtistRequestDTO, ArtistResponseDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Artist toEntity(ArtistRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Artist.builder()
                .name(requestDTO.getName())
                .type(requestDTO.getType())
                .albums(new ArrayList<>())
                .build();
    }

    @Override
    public ArtistResponseDTO toResponseDTO(Artist entity) {
        if (entity == null) {
            return null;
        }
        ArtistResponseDTO dto = modelMapper.map(entity, ArtistResponseDTO.class);
        if (dto.getAlbums() == null) {
            dto.setAlbums(new ArrayList<>());
        }
        return dto;
    }

    public ArtistResponseDTO toResponseDTOWithAlbums(Artist entity, AlbumMapper albumMapper) {
        if (entity == null) {
            return null;
        }
        ArtistResponseDTO dto = toResponseDTO(entity);
        if (entity.getAlbums() != null && !entity.getAlbums().isEmpty()) {
            dto.setAlbums(entity.getAlbums().stream()
                    .map(albumMapper::toResponseDTOSimple)
                    .toList());
        }
        return dto;
    }

    @Override
    public void updateEntityFromDTO(ArtistRequestDTO requestDTO, Artist entity) {
        if (requestDTO == null || entity == null) {
            return;
        }

        if (requestDTO.getName() != null) {
            entity.setName(requestDTO.getName());
        }

        if (requestDTO.getType() != null) {
            entity.setType(requestDTO.getType());
        }
    }
}