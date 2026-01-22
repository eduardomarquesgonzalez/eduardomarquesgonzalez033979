package br.gov.mt.seplag.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_albums")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
}
