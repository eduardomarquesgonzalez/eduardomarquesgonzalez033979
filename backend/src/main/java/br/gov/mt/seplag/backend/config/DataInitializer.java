package br.gov.mt.seplag.backend.config;

import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.entity.User;
import br.gov.mt.seplag.backend.repository.AlbumRepository;
import br.gov.mt.seplag.backend.repository.ArtistRepository;
import br.gov.mt.seplag.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("🚀 Iniciando carga de dados padrão...");

            initUsers();
            initArtistsAndAlbums();

            log.info("Carga de dados padrão concluída!");
            log.info("Total de usuários: {}", userRepository.count());
            log.info("Total de artistas: {}", artistRepository.count());
            log.info("Total de álbuns: {}", albumRepository.count());
        };
    }

    private void initUsers() {
        log.info("👥 Inicializando usuários...");

        // Criar usuário ADMIN se não existir
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@seplag.mt.gov.br")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.UserRole.ADMIN)
                    .enabled(true)
                    .build();

            userRepository.save(admin);
            log.info("Usuário ADMIN criado (username: admin, senha: admin123)");
        } else {
            log.info("Usuário ADMIN já existe");
        }

        // Criar usuário USER se não existir
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = User.builder()
                    .username("user")
                    .email("user@seplag.mt.gov.br")
                    .password(passwordEncoder.encode("user123"))
                    .role(User.UserRole.USER)
                    .enabled(true)
                    .build();

            userRepository.save(user);
            log.info("Usuário USER criado (username: user, senha: user123)");
        } else {
            log.info("Usuário USER já existe");
        }
    }

    private void initArtistsAndAlbums() {
        log.info("Inicializando artistas e álbuns...");

        createSerjTankianAlbums();
        createMikeShinodaAlbums();
        createMichelTeloAlbums();
        createGunsNRosesAlbums();
    }

    private void createSerjTankianAlbums() {
        Artist artist = findOrCreateArtist("Serj Tankian", "Solo");

        createAlbumIfNotExists(artist, "Harakiri", LocalDate.of(2012, 7, 10),
                12, "Segundo álbum solo de Serj Tankian, vocalista do System of a Down");

        createAlbumIfNotExists(artist, "Black Blooms", LocalDate.of(2024, 1, 26),
                15, "EP instrumental com composições experimentais");

        createAlbumIfNotExists(artist, "The Rough Dog", LocalDate.of(2023, 3, 24),
                10, "Álbum conceitual com influências de rock alternativo");

        log.info("Artista: Serj Tankian - 3 álbuns");
    }

    private void createMikeShinodaAlbums() {
        Artist artist = findOrCreateArtist("Mike Shinoda", "Solo");

        createAlbumIfNotExists(artist, "The Rising Tied", LocalDate.of(2005, 10, 24),
                16, "Álbum de estreia do projeto Fort Minor");

        createAlbumIfNotExists(artist, "Post Traumatic", LocalDate.of(2018, 6, 15),
                16, "Álbum solo criado após a morte de Chester Bennington");

        createAlbumIfNotExists(artist, "Post Traumatic EP", LocalDate.of(2018, 1, 25),
                3, "EP com faixas iniciais do projeto Post Traumatic");

        createAlbumIfNotExists(artist, "Where'd You Go", LocalDate.of(2006, 4, 14),
                1, "Single de sucesso do Fort Minor");

        log.info("Artista: Mike Shinoda - 4 álbuns");
    }

    private void createMichelTeloAlbums() {
        Artist artist = findOrCreateArtist("Michel Teló", "Solo");

        createAlbumIfNotExists(artist, "Bem Sertanejo", LocalDate.of(2013, 9, 30),
                14, "Projeto que marcou nova fase da carreira do artista");

        createAlbumIfNotExists(artist, "Bem Sertanejo - O Show (Ao Vivo)", LocalDate.of(2014, 6, 3),
                22, "Registro ao vivo do show Bem Sertanejo");

        createAlbumIfNotExists(artist, "Bem Sertanejo - (1ª Temporada) - EP", LocalDate.of(2013, 7, 26),
                6, "EP com primeiras faixas do projeto Bem Sertanejo");

        log.info("Artista: Michel Teló - 3 álbuns");
    }

    private void createGunsNRosesAlbums() {
        Artist artist = findOrCreateArtist("Guns N' Roses", "Banda");

        createAlbumIfNotExists(artist, "Use Your Illusion I", LocalDate.of(1991, 9, 17),
                16, "Primeiro dos álbuns duplos lançados simultaneamente");

        createAlbumIfNotExists(artist, "Use Your Illusion II", LocalDate.of(1991, 9, 17),
                14, "Segundo dos álbuns duplos lançados simultaneamente");

        createAlbumIfNotExists(artist, "Greatest Hits", LocalDate.of(2004, 3, 23),
                14, "Coletânea com os maiores sucessos da banda");

        log.info("Artista: Guns N' Roses - 3 álbuns");
    }

    private Artist findOrCreateArtist(String name, String type) {
        return artistRepository.findByName(name)
                .orElseGet(() -> {
                    Artist artist = Artist.builder()
                            .name(name)
                            .type(type)
                            .build();
                    return artistRepository.save(artist);
                });
    }

    private void createAlbumIfNotExists(Artist artist, String title, LocalDate releaseDate,
                                        Integer numberOfTracks, String description) {
        if (albumRepository.findByTitleAndArtist(title, artist).isEmpty()) {
            Album album = Album.builder()
                    .title(title)
                    .releaseDate(releaseDate)
                    .numberOfTracks(numberOfTracks)
                    .description(description)
                    .artist(artist)
                    .build();

            albumRepository.save(album);
        }
    }
}