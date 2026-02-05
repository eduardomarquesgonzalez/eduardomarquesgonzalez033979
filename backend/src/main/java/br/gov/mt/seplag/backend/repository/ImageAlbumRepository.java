package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.ImageAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageAlbumRepository extends JpaRepository<ImageAlbum, Long> {
}