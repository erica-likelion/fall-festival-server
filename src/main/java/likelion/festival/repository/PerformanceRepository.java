package likelion.festival.repository;

import likelion.festival.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 공연 데이터 접근 레포지토리
 */
@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    /**
     * 모든 공연 정보를 가수 정보와 함께 조회
     */
    @Query("SELECT p FROM Performance p JOIN FETCH p.artist ORDER BY p.startTime")
    List<Performance> findAllWithArtist();
}
