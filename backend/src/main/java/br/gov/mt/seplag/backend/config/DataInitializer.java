package br.gov.mt.seplag.backend.config;

import br.gov.mt.seplag.backend.entity.User;
import br.gov.mt.seplag.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @Bean
    public CommandLineRunner initDefaultUsers() {
        return args -> {
            // Criar usuário ADMIN se não existir
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@seplag.mt.gov.br")
                        .password(passwordEncoderConfig.passwordEncoder().encode("admin123"))
                        .role(User.UserRole.ADMIN)
                        .enabled(true)
                        .build();

                userRepository.save(admin);
                log.info("Usuário ADMIN criado com sucesso! (username: admin, senha: admin123)");
            } else {
                log.info("Usuário ADMIN já existe no banco de dados");
            }

            // Criar usuário USER se não existir
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = User.builder()
                        .username("user")
                        .email("user@seplag.mt.gov.br")
                        .password(passwordEncoderConfig.passwordEncoder().encode("user123"))
                        .role(User.UserRole.USER)
                        .enabled(true)
                        .build();

                userRepository.save(user);
                log.info("Usuário USER criado com sucesso! (username: user, senha: user123)");
            } else {
                log.info("Usuário USER já existe no banco de dados");
            }

            log.info("Inicialização de usuários padrão concluída!");
        };
    }
}