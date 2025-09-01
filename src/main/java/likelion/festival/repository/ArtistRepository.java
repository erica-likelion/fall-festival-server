package likelion.festival.repository;

import likelion.festival.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 가수 데이터 접근 레포지토리
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * 가수 정보와 대표곡을 함께 조회
     */
    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.songs WHERE a.id = :id")
    Optional<Artist> findByIdWithSongs(@Param("id") Long id);
}
