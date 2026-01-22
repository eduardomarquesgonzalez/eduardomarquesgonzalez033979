package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByName(String name);

    boolean existsByName(String name);
}
