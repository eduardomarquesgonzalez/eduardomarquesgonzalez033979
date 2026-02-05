package br.gov.mt.seplag.backend.repository.projection;

public interface AlbumProjection {
    Long getId();
    String getTitle();
    String getCoverUrl();
    Long getArtistCount();
    Long getImageCount();
}