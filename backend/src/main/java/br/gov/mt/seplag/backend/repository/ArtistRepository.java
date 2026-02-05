package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Artist;
import br.gov.mt.seplag.backend.repository.projection.ArtistProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query(value = """
            SELECT 
                a.id as id, 
                a.name as name, 
                COALESCE(COUNT(al.id), 0) as albumCount 
            FROM artists a 
            LEFT JOIN album_artist aa ON a.id = aa.artist_id 
            LEFT JOIN albums al ON aa.album_id = al.id 
            GROUP BY a.id, a.name
            ORDER BY a.name
            """,
            countQuery = "SELECT COUNT(*) FROM artists",
            nativeQuery = true)
    Page<ArtistProjection> findAllWithAlbumCount(Pageable pageable);

    @Query(value = """
            SELECT 
                a.id as id, 
                a.name as name, 
                COALESCE(COUNT(al.id), 0) as albumCount 
            FROM artists a 
            LEFT JOIN album_artist aa ON a.id = aa.artist_id 
            LEFT JOIN albums al ON aa.album_id = al.id 
            WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))
            GROUP BY a.id, a.name
            ORDER BY a.name
            """,
            countQuery = """
                    SELECT COUNT(*) FROM artists 
                    WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))
                    """,
            nativeQuery = true)
    Page<ArtistProjection> findByNameWithAlbumCount(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Artist a LEFT JOIN FETCH a.albums WHERE a.id = :id")
    Optional<Artist> findByIdWithAlbums(@Param("id") Long id);
}