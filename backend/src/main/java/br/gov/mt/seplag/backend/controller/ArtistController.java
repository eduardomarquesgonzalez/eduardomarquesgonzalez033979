package br.gov.mt.seplag.backend.controller;

import br.gov.mt.seplag.backend.dto.PageResponseDTO;
import br.gov.mt.seplag.backend.dto.request.ArtistRequestDTO;
import br.gov.mt.seplag.backend.dto.response.AlbumResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistDetailResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ArtistResponseDTO;
import br.gov.mt.seplag.backend.dto.response.ErrorResponse;
import br.gov.mt.seplag.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/artists")
@Tag(name = "Artists", description = "Gerenciamento de artistas")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @Operation(
            summary = "Listar todos os artistas",
            description = "Retorna lista paginada de artistas com contagem de álbuns e filtro opcional por nome"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    @GetMapping
    public ResponseEntity<PageResponseDTO<ArtistResponseDTO>> findAll(
            @Parameter(description = "Filtro por nome do artista (busca parcial, case-insensitive)")
            @RequestParam(required = false) String name,
            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.debug("GET /api/v1/artists - name: '{}', page: {}, size: {}",
                name, pageable.getPageNumber(), pageable.getPageSize());

        Page<ArtistResponseDTO> page = artistService.findAll(name, pageable);
        PageResponseDTO<ArtistResponseDTO> response = PageResponseDTO.from(page);

        log.debug("Retornando {} artistas de um total de {}",
                response.content().size(), response.totalElements());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar artista por ID",
            description = "Retorna detalhes completos do artista incluindo lista de álbuns"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artista não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDetailResponseDTO> findById(
            @Parameter(description = "ID do artista", required = true)
            @PathVariable Long id) {

        log.debug("GET /api/v1/artists/{}", id);

        ArtistDetailResponseDTO artist = artistService.findByIdWithAlbums(id);
        return ResponseEntity.ok(artist);
    }

    @Operation(
            summary = "Listar álbuns de um artista",
            description = "Retorna lista paginada de álbuns pertencentes ao artista especificado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    })
    @GetMapping("/{id}/albums")
    public ResponseEntity<PageResponseDTO<AlbumResponseDTO>> findAlbumsByArtist(
            @Parameter(description = "ID do artista", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC)
            Pageable pageable) {

        log.debug("GET /api/v1/artists/{}/albums - page: {}, size: {}",
                id, pageable.getPageNumber(), pageable.getPageSize());

        Page<AlbumResponseDTO> page = artistService.findAlbumsByArtist(id, pageable);
        PageResponseDTO<AlbumResponseDTO> response = PageResponseDTO.from(page);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Criar novo artista",
            description = "Cria um novo artista no catálogo musical"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Artista criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ArtistResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do artista a ser criado",
                    required = true
            )
            @RequestBody @Valid ArtistRequestDTO dto) {

        log.info("POST /api/v1/artists - Criando artista: '{}'", dto.name());

        ArtistResponseDTO response = artistService.create(dto);

        log.info("Artista criado com sucesso - ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar artista",
            description = "Atualiza os dados de um artista existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artista não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> update(
            @Parameter(description = "ID do artista a ser atualizado", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados do artista",
                    required = true
            )
            @RequestBody @Valid ArtistRequestDTO dto) {

        log.info("PUT /api/v1/artists/{} - Atualizando para: '{}'", id, dto.name());

        ArtistResponseDTO response = artistService.update(id, dto);

        log.info("Artista atualizado com sucesso - ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deletar artista",
            description = "Remove um artista do catálogo (se não possuir álbuns associados)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Artista deletado com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artista não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Artista possui álbuns associados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do artista a ser deletado", required = true)
            @PathVariable Long id) {

        log.info("DELETE /api/v1/artists/{}", id);

        artistService.delete(id);

        log.info("Artista deletado com sucesso - ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}