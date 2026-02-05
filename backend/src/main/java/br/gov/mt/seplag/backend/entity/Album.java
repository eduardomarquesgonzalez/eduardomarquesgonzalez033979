package br.gov.mt.seplag.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)

    private Set<ImageAlbum> coverImages;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "album_artist",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )

    private Set<Artist> artists;

    public void addImage(ImageAlbum image) {
        if (image == null) {
            return;
        }
        coverImages.add(image);
        image.setAlbum(this);
        if (this.coverUrl == null && !coverImages.isEmpty()) {
            this.coverUrl = image.getObjectKey();
        }
    }

    public void addArtistAlbum(Artist artist) {
        if (artist == null) {
            return;
        }

        this.artists.add(artist);
        artist.getAlbums().add(this);
    }

    public void clearArtists() {
    }
}
