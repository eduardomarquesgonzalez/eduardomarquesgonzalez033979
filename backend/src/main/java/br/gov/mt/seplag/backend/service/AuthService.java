package br.gov.mt.seplag.backend.service;


import br.gov.mt.seplag.backend.dto.request.LoginRequest;
import br.gov.mt.seplag.backend.dto.request.RegisterRequest;
import br.gov.mt.seplag.backend.dto.response.AuthResponse;
import br.gov.mt.seplag.backend.dto.response.UserResponse;
import br.gov.mt.seplag.backend.entity.User;
import br.gov.mt.seplag.backend.enums.Role;
import br.gov.mt.seplag.backend.exception.DataIntegrityViolationException;
import br.gov.mt.seplag.backend.exception.ObjectnotFoundException;
import br.gov.mt.seplag.backend.repository.UserRepository;
import br.gov.mt.seplag.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DataIntegrityViolationException("Username já está em uso");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ObjectnotFoundException("Usuário não encontrado"));
        user.updateLastLogin();
        userRepository.save(user);
        // Gera o token de acesso (5 minutos)
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.of(
                jwtToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                jwtService.getExpirationTime() / 1000 // converte para segundos
        );
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectnotFoundException("Usuário não encontrado"));
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new ObjectnotFoundException("Refresh token inválido ou expirado");
        }
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.of(
                newAccessToken,
                newRefreshToken,
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                jwtService.getExpirationTime() / 1000
        );
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
