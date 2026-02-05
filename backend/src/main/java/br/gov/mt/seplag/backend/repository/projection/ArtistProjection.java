package br.gov.mt.seplag.backend.repository.projection;

public interface ArtistProjection {
    Long getId();

    String getName();

    Long getAlbumCount();
}