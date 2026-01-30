package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.mapper.ArtistMapper;
import br.gov.mt.seplag.backend.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final AlbumService albumService;

    public ArtistResponseDTO create(ArtistRequestDTO dto) {
        Artist artist = ArtistMapper.toEntity(dto);
        return ArtistMapper.toResponse(
                artistRepository.save(artist)
        );
    }

    public Page<ArtistResponseDTO> findAll(
            String name, String sort, Pageable pageable) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "name"));
        Page<Artist> page = (name == null || name.isBlank())
                ? artistRepository.findAll(sortedPageable)
 : artistRepository.findByNameContainingIgnoreCase(
                name, sortedPageable);

        return page.map(ArtistMapper::toResponse);
    }

    public Page<AlbumResponseDTO> findAlbumsByArtist(
            Long artistId, Pageable pageable) {

        return albumService.findByArtistId(artistId, pageable);
    }
}
