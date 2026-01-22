package br.gov.mt.seplag.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "albums")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;


    @Column(name = "number_of_tracks")
    private Integer numberOfTracks;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageAlbum> coverImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

}
