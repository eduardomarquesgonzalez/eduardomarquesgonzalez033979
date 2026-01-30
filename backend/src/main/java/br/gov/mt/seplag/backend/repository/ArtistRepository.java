package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Page<Artist> findByNameContainingIgnoreCase(
            String name, Pageable pageable);
}
