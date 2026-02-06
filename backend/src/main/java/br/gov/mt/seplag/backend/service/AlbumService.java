package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.request.AlbumRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumDetailResponseDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.entity.ImageAlbum;
import br.gov.mt.seplag.backend.exception.ObjectnotFoundException;
import br.gov.mt.seplag.backend.mapper.AlbumMapper;
import br.gov.mt.seplag.backend.repository.AlbumRepository;
import br.gov.mt.seplag.backend.repository.ArtistRepository;
import br.gov.mt.seplag.backend.repository.ImageAlbumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final ImageAlbumRepository imageAlbumRepository;
    private final MinioStorageService storageService;
    private final AlbumMapper albumMapper;

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> findAll(String title, Long artistId, Pageable pageable) {
        Page<Album> page;

        if (title != null && artistId != null) {
            page = albumRepository.findByTitleAndArtist(title, artistId, pageable);
        } else if (title != null) {
            page = albumRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (artistId != null) {
            page = albumRepository.findByArtistId(artistId, pageable);
        } else {
            page = albumRepository.findAll(pageable);
        }

        return page.map(albumMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public AlbumDetailResponseDTO findById(Long id) {
        Album album = albumRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ObjectnotFoundException("Álbum não encontrado. ID: " + id));

        return albumMapper.toDetailResponse(album);
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> findByArtistId(Long artistId, Pageable pageable) {
        log.debug("Buscando álbuns do artista ID: {}, page: {}",
                artistId, pageable.getPageNumber());
        if (!artistRepository.existsById(artistId)) {
            throw new ObjectnotFoundException("Artista não encontrado. ID: " + artistId);
        }
        Page<Album> page = albumRepository.findByArtistId(artistId, pageable);
        log.debug("Encontrados {} álbuns para o artista ID: {}",
                page.getTotalElements(), artistId);
        return page.map(albumMapper::toResponse);
    }

    @Transactional
    public AlbumResponseDTO create(AlbumRequestDTO dto, List<MultipartFile> images) {
        log.info("Criando álbum: '{}'", dto.title());
        Album album = albumMapper.toEntity(dto);
        Set<Artist> artists = dto.artistIds().stream()
                .map(artistId -> artistRepository.findById(artistId).orElseThrow(() -> new ObjectnotFoundException("Artista não encontrado. ID: " + artistId)))
                .collect(Collectors.toSet());
        artists.forEach(album::addArtist);
        album = albumRepository.save(album);
        if (images != null && !images.isEmpty()) {
            uploadImages(album, images);
        }
        log.info("Álbum criado - ID: {}, imagens: {}", album.getId(),
                album.getCoverImages() != null ? album.getCoverImages().size() : 0);
        return albumMapper.toResponse(album);
    }

    @Transactional
    public AlbumResponseDTO update(Long id, AlbumRequestDTO dto, List<MultipartFile> newImages) {
        log.info("Atualizando álbum ID: {}", id);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ObjectnotFoundException("Álbum não encontrado. ID: " + id));
        if (dto.title() != null && !dto.title().isBlank()) {
            album.setTitle(dto.title());
        }
        if (dto.artistIds() != null && !dto.artistIds().isEmpty()) {
            album.clearArtists();
            Album finalAlbum = album;
            dto.artistIds().forEach(artistId -> {
                Artist artist = artistRepository.findById(artistId)
                        .orElseThrow(() -> new ObjectnotFoundException("Artista não encontrado. ID: " + artistId));
                finalAlbum.addArtist(artist);
            });
        }
        if (newImages != null && !newImages.isEmpty()) {
            uploadImages(album, newImages);
        }
        album = albumRepository.save(album);
        log.info("Álbum atualizado - ID: {}", id);
        return albumMapper.toResponse(album);
    }

    private void uploadImages(Album album, List<MultipartFile> images) {
        String folder = album.getId().toString();
        for (MultipartFile file : images) {
            if (file.isEmpty()) {
                continue;
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Arquivo deve ser uma imagem: " + file.getOriginalFilename());
            }
            String fileUrl = storageService.upload(file, folder);
            ImageAlbum imageAlbum = ImageAlbum.builder()
                    .objectKey(fileUrl)
                    .fileName(file.getOriginalFilename())
                    .contentType(contentType)
                    .album(album)
                    .build();

            album.addImage(imageAlbum);
            log.info("Imagem adicionada: {} → {}", file.getOriginalFilename(), fileUrl);
        }
    }
}