package br.gov.mt.seplag.backend.controller;

import br.gov.mt.seplag.backend.dto.request.AlbumRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/albums")
@Tag(name = "Albums")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    @Operation(summary = "Cadastrar álbum")
    public ResponseEntity<AlbumResponseDTO> create(@RequestBody @Valid AlbumRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar álbum")
    public ResponseEntity<AlbumResponseDTO> update(@PathVariable Long id, @RequestBody @Valid AlbumRequestDTO dto) {
        return ResponseEntity.ok(albumService.update(id, dto));
    }

    @GetMapping
    @Operation(summary = "Consultar álbuns",
            description = "Consulta álbuns com paginação e filtros por título e artista")
    public ResponseEntity<Page<AlbumResponseDTO>> findAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artistName, Pageable pageable) {
        return ResponseEntity.ok(albumService.findAll(title, artistName, pageable));
    }

    @GetMapping("/artist")
    public ResponseEntity<List<AlbumResponseDTO>> findByArtistName(
            @RequestParam String artistName, @RequestParam(defaultValue = "ASC") String direction) {
        List<AlbumResponseDTO> albums = albumService.findByArtistName(artistName, direction);
        return ResponseEntity.ok(albums);
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> uploadImages(@PathVariable Long id, @RequestPart("files") List<MultipartFile> files) {
        albumService.uploadImages(id, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
