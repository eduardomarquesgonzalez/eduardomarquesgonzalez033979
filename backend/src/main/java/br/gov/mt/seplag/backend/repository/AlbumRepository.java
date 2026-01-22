package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtist(Artist artist);

    Optional<Album> findByTitleAndArtist(String title, Artist artist);

    boolean existsByTitleAndArtist(String title, Artist artist);
}
