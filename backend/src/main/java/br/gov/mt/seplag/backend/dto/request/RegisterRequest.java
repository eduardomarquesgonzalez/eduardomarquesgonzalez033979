package br.gov.mt.seplag.backend.dto.request;

import br.gov.mt.seplag.backend.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username é obrigatório")
        @Size(min = 3, max = 100, message = "Username deve ter entre 3 e 100 caracteres")
        String username,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Password deve ter no mínimo 6 caracteres")
        String password,

        Role role
) {
}