package br.gov.mt.seplag.backend.mapper;

import br.gov.mt.seplag.backend.dto.request.AlbumRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumDetailResponseDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistSimpleDTO;
import br.gov.mt.seplag.backend.dto.response.ImageDTO;
import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.entity.ImageAlbum;
import br.gov.mt.seplag.backend.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Mapper para conversão entre entidades Album e seus DTOs.
 * Responsável por gerar URLs pré-assinadas do MinIO com expiração de 30 minutos.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumMapper {

    private final MinioStorageService storageService;
    public Album toEntity(AlbumRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Album.builder()
                .title(dto.title())
                .build();
    }

    public AlbumResponseDTO toResponse(Album album) {
        if (album == null) {
            return null;
        }

        // URL da capa principal (primeira imagem) com expiração de 30min
        String coverUrl = generateCoverUrl(album.getCoverUrl());

        List<String> artistNames = extractArtistNames(album.getArtists());
        int imageCount = (album.getCoverImages() != null)
                ? album.getCoverImages().size()
                : 0;
        return AlbumResponseDTO.builder()
                .id(album.getId())
                .title(album.getTitle())
                .coverUrl(coverUrl)
                .artistNames(artistNames)
                .imageCount(imageCount)
                .build();
    }

    public AlbumDetailResponseDTO toDetailResponse(Album album) {
        if (album == null) {
            return null;
        }
        log.debug("Convertendo album ID: {} para DTO detalhado", album.getId());
        String coverUrl = generateCoverUrl(album.getCoverUrl());
        List<ArtistSimpleDTO> artists = mapArtistsToDTO(album.getArtists());
        List<ImageDTO> images = mapImagesToDTO(album);

        return AlbumDetailResponseDTO.builder()
                .id(album.getId())
                .title(album.getTitle())
                .coverUrl(coverUrl)
                .artists(artists)
                .images(images)
                .build();
    }

    private String generateCoverUrl(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            return null;
        }

        try {
            return storageService.generatePresignedUrl(objectKey, 30);
        } catch (Exception e) {
            log.warn("Erro ao gerar URL pré-assinada para objectKey: {}", objectKey, e);
            return null;
        }
    }

    private List<String> extractArtistNames(Set<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            return Collections.emptyList();
        }

        return artists.stream()
                .map(Artist::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private List<ArtistSimpleDTO> mapArtistsToDTO(Set<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            return Collections.emptyList();
        }

        return artists.stream()
                .map(artist -> ArtistSimpleDTO.builder()
                        .id(artist.getId())
                        .name(artist.getName())
                        .build())
                .sorted(Comparator.comparing(ArtistSimpleDTO::name, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private List<ImageDTO> mapImagesToDTO(Album album) {
        if (album.getCoverImages() == null || album.getCoverImages().isEmpty()) {
            return Collections.emptyList();
        }

        String primaryCoverUrl = album.getCoverUrl();

        return album.getCoverImages().stream()
                .map(image -> mapSingleImageToDTO(image, primaryCoverUrl))
                .filter(dto -> dto != null)  // Remove DTOs com erro de URL
                .sorted(Comparator
                        .comparing(ImageDTO::isPrimary).reversed()
                        .thenComparing(ImageDTO::fileName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private ImageDTO mapSingleImageToDTO(ImageAlbum image, String primaryCoverUrl) {
        String url = null;

        try {
            // Requisito (i): URL pré-assinada com expiração de 30 minutos
            url = storageService.generatePresignedUrl(image.getObjectKey(), 30);
        } catch (Exception e) {
            log.warn("Erro ao gerar URL para imagem ID: {} ({})",
                    image.getId(), image.getObjectKey(), e);
            return null;
        }

        boolean isPrimary = image.getObjectKey().equals(primaryCoverUrl);

        return ImageDTO.builder()
                .id(image.getId())
                .fileName(image.getFileName())
                .url(url)
                .contentType(image.getContentType())
                .isPrimary(isPrimary)
                .build();
    }

    public List<AlbumResponseDTO> toResponseList(List<Album> albums) {
        if (albums == null || albums.isEmpty()) {
            return Collections.emptyList();
        }

        return albums.stream()
                .map(this::toResponse)
                .toList();
    }
}