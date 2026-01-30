package br.gov.mt.seplag.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @ManyToMany(mappedBy = "artists")
    @Builder.Default
    private List<Album> albums = new ArrayList<>();

    public void addAlbum(Album album) {
        this.albums.add(album);
        album.getArtists().add(this);
    }

    public void removeAlbum(Album album) {
        this.albums.remove(album);
        album.getArtists().remove(this);
    }
}
