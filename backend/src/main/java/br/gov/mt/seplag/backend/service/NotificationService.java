package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyNewAlbum(Long albumId, String albumTitle, String artistName) {
        log.info("Enviando notificação de novo álbum: {} - {}", artistName, albumTitle);
        NotificationDTO notification = NotificationDTO.builder()
                .type("NEW_ALBUM")
                .data(Map.of(
                        "id", albumId,
                        "title", albumTitle,
                        "artist", artistName,
                        "message", "Novo álbum adicionado!"
                ))
                .timestamp(LocalDateTime.now())
                .build();
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        log.info("Notificação enviada para /topic/notifications");
    }

    public void notifyArtistUpdated(Long artistId, String artistName) {
        log.info("Enviando notificação de atualização de artista: {}", artistName);
        NotificationDTO notification = NotificationDTO.builder()
                .type("ARTIST_UPDATED")
                .data(Map.of(
                        "id", artistId,
                        "name", artistName,
                        "message", "Artista atualizado!"
                ))
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    public void notifyAlbumDeleted(Long albumId, String albumTitle) {
        log.info("Enviando notificação de álbum deletado: {}", albumTitle);
        Map<String, Object> message = new HashMap<>();
        message.put("type", "ALBUM_DELETED");
        message.put("data", Map.of(
                "id", albumId,
                "title", albumTitle,
                "message", "Álbum removido"
        ));
        message.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/notifications", (Object) message);
    }
}