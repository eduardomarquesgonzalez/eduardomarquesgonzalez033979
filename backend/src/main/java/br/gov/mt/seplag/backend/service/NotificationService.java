package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.NotificationDTO;
import br.gov.mt.seplag.backend.entity.Album;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyAlbumCreated(Album album) {
        log.info("Enviando notificação de novo álbum: {}", album.getTitle());

        String artistNames = album.getArtists().stream()
                .map(artist -> artist.getName())
                .collect(Collectors.joining(", "));

        NotificationDTO notification = NotificationDTO.builder()
                .type("ALBUM_CREATED")
                .data(Map.of(
                        "id", album.getId(),
                        "title", album.getTitle(),
                        "artist", artistNames,
                        "coverUrl", album.getCoverUrl() != null ? album.getCoverUrl() : "",
                        "message", "Novo álbum adicionado: " + album.getTitle()
                ))
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications", notification);
        log.info("Notificação enviada para /topic/notifications - ALBUM_CREATED");
    }

    public void notifyAlbumUpdated(Album album) {
        log.info("Enviando notificação de álbum atualizado: {}", album.getTitle());

        String artistNames = album.getArtists().stream()
                .map(artist -> artist.getName())
                .collect(Collectors.joining(", "));

        NotificationDTO notification = NotificationDTO.builder()
                .type("ALBUM_UPDATED")
                .data(Map.of(
                        "id", album.getId(),
                        "title", album.getTitle(),
                        "artist", artistNames,
                        "coverUrl", album.getCoverUrl() != null ? album.getCoverUrl() : "",
                        "message", "Álbum atualizado: " + album.getTitle()
                ))
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications", notification);
        log.info("Notificação enviada para /topic/notifications - ALBUM_UPDATED");
    }


    public void notifyArtistUpdated(Long artistId, String artistName) {
        log.info("Enviando notificação de atualização de artista: {}", artistName);

        NotificationDTO notification = NotificationDTO.builder()
                .type("ARTIST_UPDATED")
                .data(Map.of(
                        "id", artistId,
                        "name", artistName,
                        "message", "Artista atualizado: " + artistName
                ))
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications", notification);
        log.info("Notificação enviada para /topic/notifications - ARTIST_UPDATED");
    }

    public void notifyArtistCreated(Long artistId, String artistName) {
        log.info("Enviando notificação de novo artista: {}", artistName);

        NotificationDTO notification = NotificationDTO.builder()
                .type("ARTIST_CREATED")
                .data(Map.of(
                        "id", artistId,
                        "name", artistName,
                        "message", "Novo artista cadastrado: " + artistName
                ))
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications", notification);
        log.info("Notificação enviada para /topic/notifications - ARTIST_CREATED");
    }

}