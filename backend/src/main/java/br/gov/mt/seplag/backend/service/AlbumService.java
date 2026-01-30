package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.request.AlbumRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.entity.ImageAlbum;
import br.gov.mt.seplag.backend.mapper.AlbumMapper;
import br.gov.mt.seplag.backend.repository.AlbumRepository;
import br.gov.mt.seplag.backend.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private AlbumMapper albumMapper;
    private MinioStorageService storageService;

    @Transactional(readOnly = true)
    public AlbumResponseDTO findById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));

        return albumMapper.toResponseDTO(album);
    }

    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> findByArtistName(String artistName, String direction) {
        Sort sort = Sort.by("title");
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        List<Album> albums = albumRepository.findByArtists_NameIgnoreCaseContaining(artistName, sort);
        return albums.stream().map(albumMapper::toResponseDTO).toList();
    }


    public AlbumResponseDTO create(AlbumRequestDTO dto) {
        Set<Artist> artists = new HashSet<>(artistRepository.findAllById(dto.getArtistIds()).stream().collect(Collectors.toSet()));

        if (artists.size() != dto.getArtistIds().size()) {
            throw new EntityNotFoundException("Um ou mais artistas não encontrados");
        }

        Album album = Album.builder()
                .title(dto.getTitle())
                .artists(artists)
                .build();

        Album saved = albumRepository.save(album);
        return albumMapper.toResponseDTO(saved);
    }

    @Transactional
    public AlbumResponseDTO update(Long id, AlbumRequestDTO dto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));
        Set<Artist> artists = artistRepository.findAllById(dto.getArtistIds())
                .stream().collect(Collectors.toSet());
        if (artists.size() != dto.getArtistIds().size()) {
            throw new EntityNotFoundException("Um ou mais artistas não encontrados");
        }
        album.setTitle(dto.getTitle());
        album.getArtists().clear();
        album.getArtists().addAll(artists);
        return albumMapper.toResponseDTO(album);
    }

    public Page<AlbumResponseDTO> findByArtistId(Long artistId, Pageable pageable) {
        return albumRepository.findByArtistId(artistId, pageable).map(albumMapper::toResponseDTO);
    }

    public Page<AlbumResponseDTO> findAll(String title, String artistName, Pageable pageable) {
        Page<Album> page = albumRepository.search(normalize(title), normalize(artistName), pageable);
        return page.map(albumMapper::toResponseDTO);
    }

    @Transactional
    public void uploadImages(Long albumId, List<MultipartFile> files) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));
        for (MultipartFile file : files) {
            String objectKey = storageService.upload(file);
            ImageAlbum image = ImageAlbum.builder()
                    .objectKey(objectKey).fileName(file.getOriginalFilename())
                    .contentType(file.getContentType()).album(album).build();
            album.getCoverImages().add(image);
        }
    }

    private String normalize(String value) {
        return (value == null || value.isBlank())
                ? null
                : value.trim();
    }
}
