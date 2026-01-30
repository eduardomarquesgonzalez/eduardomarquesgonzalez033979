package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByArtists_NameIgnoreCaseContaining(String artistName, Sort sort);

    @Query("""
                select distinct a from Album a
                left join fetch a.artists ar
                left join fetch a.coverImages img
                where a.id = :id
            """)
    Optional<Album> findByIdWithRelations(@Param("id") Long id);

    @Query("""
                select distinct a from Album a
                join a.artists ar
                where (:title is null or lower(a.title) like lower(concat('%', :title, '%')))
                  and (:artistName is null or lower(ar.name) like lower(concat('%', :artistName, '%')))
            """)
    Page<Album> search(@Param("title") String title, @Param("artistName") String artistName, Pageable pageable);

    @Query("""
                select a from Album a
                join a.artists ar
                where ar.id = :artistId
            """)
    Page<Album> findByArtistId(@Param("artistId") Long artistId, Pageable pageable);
}
