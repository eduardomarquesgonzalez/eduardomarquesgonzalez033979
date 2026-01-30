package br.gov.mt.seplag.backend.repository;

import java.util.Collection;

public interface ImageAlbumRepository {
    Collection<Object> findByAlbumId(Long albumId);
}
