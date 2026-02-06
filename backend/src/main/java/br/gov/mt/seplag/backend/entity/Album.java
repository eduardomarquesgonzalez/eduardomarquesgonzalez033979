package br.gov.mt.seplag.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "albums")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"artists", "coverImages"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"artists", "coverImages"})
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ImageAlbum> coverImages = new HashSet<>();

    @ManyToMany(mappedBy = "albums", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Artist> artists = new HashSet<>();

    public void addImage(ImageAlbum image) {
        if (image == null) {
            return;
        }
        if (this.coverImages == null) {
            this.coverImages = new HashSet<>();
        }
        coverImages.add(image);
        image.setAlbum(this);
        if (this.coverUrl == null && !coverImages.isEmpty()) {
            this.coverUrl = image.getObjectKey();
        }
    }

    public void addArtist(Artist artist) {
        if (artist == null) {
            return;
        }
        if (this.artists == null) {
            this.artists = new HashSet<>();
        }
        if (artist.getAlbums() == null) {
            artist.setAlbums(new HashSet<>());
        }
        this.artists.add(artist);
        artist.getAlbums().add(this);
    }

    public void clearArtists() {
        if (this.artists != null) {
            this.artists.forEach(artist -> artist.getAlbums().remove(this));
            this.artists.clear();
        }
    }
}