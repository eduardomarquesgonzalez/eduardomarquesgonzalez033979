package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistDetailResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.exception.ObjectnotFoundException;
import br.gov.mt.seplag.backend.mapper.ArtistMapper;
import br.gov.mt.seplag.backend.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final AlbumService albumService;
    private final ArtistMapper artistMapper;

    @Transactional(readOnly = true)
    public Page<ArtistResponseDTO> findAll(String name, Pageable pageable) {
        log.debug("Buscando artistas - filtro: '{}', página: {}", name, pageable.getPageNumber());

        var projections = (name == null || name.isBlank())
                ? artistRepository.findAllWithAlbumCount(pageable)
                : artistRepository.findByNameWithAlbumCount(name, pageable);

        return projections.map(projection -> ArtistResponseDTO.builder()
                .id(projection.getId())
                .name(projection.getName())
                .albumCount(projection.getAlbumCount().intValue()) // COALESCE garante não-null
                .build());
    }

    @Transactional(readOnly = true)
    public ArtistDetailResponseDTO findByIdWithAlbums(Long id) {
        log.info("Buscando artista com álbuns - ID: {}", id);

        Artist artist = artistRepository.findByIdWithAlbums(id)
                .orElseThrow(() -> {
                    log.error("Artista não encontrado - ID: {}", id);
                    return new ObjectnotFoundException("Artista não encontrado. ID: " + id);
                });

        // Força inicialização da coleção lazy
        int albumCount = artist.getAlbums().size();

        log.info("Artista encontrado: '{}' com {} álbuns", artist.getName(), albumCount);

        return artistMapper.toDetailResponse(artist);
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> findAlbumsByArtist(Long artistId, Pageable pageable) {
        if (!artistRepository.existsById(artistId)) {
            throw new ObjectnotFoundException("Artista não encontrado. ID: " + artistId);}
        return albumService.findByArtistId(artistId, pageable);
    }

    @Transactional
    public ArtistResponseDTO create(ArtistRequestDTO dto) {
        log.info("Criando novo artista: '{}'", dto.name());

        Artist artist = artistMapper.toEntity(dto);
        artist = artistRepository.save(artist);

        log.info("Artista criado com sucesso - ID: {}", artist.getId());
        return artistMapper.toResponse(artist);
    }

    @Transactional
    public ArtistResponseDTO update(Long id, ArtistRequestDTO dto) {
        log.info("Atualizando artista - ID: {}, novo nome: '{}'", id, dto.name());

        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ObjectnotFoundException("Artista não encontrado. ID: " + id));

        artist.setName(dto.name());
        artist = artistRepository.save(artist);

        log.info("Artista atualizado com sucesso - ID: {}", id);
        return artistMapper.toResponse(artist);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deletando artista - ID: {}", id);

        if (!artistRepository.existsById(id)) {
            throw new ObjectnotFoundException("Artista não encontrado. ID: " + id);
        }

        artistRepository.deleteById(id);
        log.info("Artista deletado com sucesso - ID: {}", id);
    }
}