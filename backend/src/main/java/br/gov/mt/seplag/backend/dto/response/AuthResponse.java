package br.gov.mt.seplag.backend.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String refreshToken,
        String type,
        Long userId,
        String username,
        String role,
        Long expiresIn
) {
    public static AuthResponse of(String token, String refreshToken, Long userId,
                                  String username, String role,
                                  Long expiresIn) {
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .userId(userId)
                .username(username)
                .role(role)
                .expiresIn(expiresIn)
                .build();
    }
}