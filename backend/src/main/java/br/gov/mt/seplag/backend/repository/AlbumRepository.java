package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Album;
import br.gov.mt.seplag.backend.repository.projection.AlbumProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Page<Album> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("""
            SELECT DISTINCT a FROM Album a 
            JOIN a.artists art 
            WHERE art.id = :artistId
            """)
    Page<Album> findByArtistId(@Param("artistId") Long artistId, Pageable pageable);

    @Query("""
            SELECT DISTINCT a FROM Album a 
            JOIN a.artists art 
            WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))
            AND art.id = :artistId
            """)
    Page<Album> findByTitleAndArtist(
            @Param("title") String title,
            @Param("artistId") Long artistId,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT a FROM Album a 
            LEFT JOIN FETCH a.artists 
            LEFT JOIN FETCH a.coverImages 
            WHERE a.id = :id
            """)
    Optional<Album> findByIdWithDetails(@Param("id") Long id);


    @Query("""
            SELECT DISTINCT a FROM Album a 
            LEFT JOIN FETCH a.artists 
            WHERE a.id = :id
            """)
    Optional<Album> findByIdWithArtists(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT a FROM Album a 
            LEFT JOIN FETCH a.coverImages 
            WHERE a.id = :id
            """)
    Optional<Album> findByIdWithImages(@Param("id") Long id);

    boolean existsByTitleIgnoreCase(String title);

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END 
            FROM Album a 
            WHERE LOWER(a.title) = LOWER(:title) 
            AND a.id != :id
            """)
    boolean existsByTitleIgnoreCaseAndIdNot(
            @Param("title") String title,
            @Param("id") Long id
    );

    @Query("""
            SELECT COUNT(DISTINCT a) FROM Album a 
            JOIN a.artists art 
            WHERE art.id = :artistId
            """)
    long countByArtistId(@Param("artistId") Long artistId);

    @Query("""
            SELECT COUNT(img) FROM Album a 
            JOIN a.coverImages img
            """)
    long countAllImages();

    @Query(value = """
            SELECT 
                a.id as id,
                a.title as title,
                a.cover_url as coverUrl,
                COUNT(DISTINCT aa.artist_id) as artistCount,
                COUNT(DISTINCT img.id) as imageCount
            FROM albums a
            LEFT JOIN album_artist aa ON a.id = aa.album_id
            LEFT JOIN image_albums img ON a.id = img.album_id
            GROUP BY a.id, a.title, a.cover_url
            ORDER BY a.title
            """,
            countQuery = "SELECT COUNT(*) FROM albums",
            nativeQuery = true)
    Page<AlbumProjection> findAllWithCounts(Pageable pageable);

}