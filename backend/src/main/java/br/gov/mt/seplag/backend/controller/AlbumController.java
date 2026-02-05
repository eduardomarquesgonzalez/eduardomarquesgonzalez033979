package br.gov.mt.seplag.backend.controller;

import br.gov.mt.seplag.backend.dto.PageResponseDTO;
import br.gov.mt.seplag.backend.dto.request.AlbumRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumDetailResponseDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/albums")
@Tag(name = "Albums", description = "Gerenciamento de álbuns musicais")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "Listar todos os álbuns")
    @GetMapping
    public ResponseEntity<PageResponseDTO<AlbumResponseDTO>> findAll(
            @Parameter(description = "Filtro por título")
            @RequestParam(required = false) String title,
            @Parameter(description = "Filtro por ID do artista")
            @RequestParam(required = false) Long artistId,
            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC)
            Pageable pageable) {
        log.debug("GET /api/v1/albums - title: '{}', artistId: {}", title, artistId);
        Page<AlbumResponseDTO> page = albumService.findAll(title, artistId, pageable);
        return ResponseEntity.ok(PageResponseDTO.from(page));
    }

    @Operation(summary = "Buscar álbum por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDetailResponseDTO> findById(@PathVariable Long id) {
        log.debug("GET /api/v1/albums/{}", id);
        return ResponseEntity.ok(albumService.findById(id));
    }
    @Operation(
            summary = "Criar novo álbum com upload de imagens",
            description = "Cria um álbum e faz upload de uma ou mais imagens de capa para o MinIO")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlbumResponseDTO> create(
            @Parameter(description = "Título do álbum", required = true)
            @RequestParam String title,
            @Parameter(description = "IDs dos artistas", required = true)
            @RequestParam Set<Long> artistIds,
            @Parameter(description = "Imagens da capa")
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        log.info("POST /api/v1/albums - title: '{}', artistIds: {}, imageCount: {}",
                title, artistIds, images != null ? images.size() : 0);
        AlbumRequestDTO dto = new AlbumRequestDTO(title, artistIds);
        AlbumResponseDTO response = albumService.create(dto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar álbum e adicionar novas imagens",
            description = "Atualiza dados do álbum e opcionalmente adiciona novas imagens")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlbumResponseDTO> update(
            @PathVariable Long id,
            @Parameter(description = "Novo título (opcional)")
            @RequestParam(required = false) String title,
            @Parameter(description = "Novos IDs de artistas (opcional)")
            @RequestParam(required = false) Set<Long> artistIds,
            @Parameter(description = "Novas imagens para adicionar (opcional)")
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        AlbumRequestDTO dto = new AlbumRequestDTO(title, artistIds);
        AlbumResponseDTO response = albumService.update(id, dto, images);
        return ResponseEntity.ok(response);
    }

}