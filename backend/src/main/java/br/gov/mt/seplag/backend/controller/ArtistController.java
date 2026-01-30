package br.gov.mt.seplag.backend.controller;

import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artists")
@Tag(name = "Artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    @Operation(summary = "Cadastrar artista")
    public ResponseEntity<ArtistResponseDTO> create(@RequestBody @Valid ArtistRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Buscar artistas por nome com ordenação")
    public ResponseEntity<Page<ArtistResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "asc") String sort, Pageable pageable) {
        return ResponseEntity.ok(artistService.findAll(name, sort, pageable));
    }

    @GetMapping("/{id}/albums")
    @Operation(summary = "Listar álbuns de um artista")
    public ResponseEntity<Page<AlbumResponseDTO>> findAlbumsByArtist(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(artistService.findAlbumsByArtist(id, pageable));
    }
}
