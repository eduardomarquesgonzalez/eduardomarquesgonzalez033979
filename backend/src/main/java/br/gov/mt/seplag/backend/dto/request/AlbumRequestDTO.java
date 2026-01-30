package br.gov.mt.seplag.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres")
    private String title;

    @NotEmpty(message = "O álbum deve ter pelo menos um artista")
    private Set<Long> artistIds;

    @NotEmpty(message = "O álbum deve ter pelo menos uma ou mais imagens da capa do álbum.")
    @NotEmpty
    private List<MultipartFile> coverImages;
}