package likelion.festival.repository;

import likelion.festival.domain.Marker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 마커 데이터 접근 레포지토리
 */
@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {

    /**
     * 모든 마커 정보를 연관된 엔티티와 함께 조회
     */
    @Query("SELECT m FROM Marker m " +
            "LEFT JOIN FETCH m.closedDays " +
            "LEFT JOIN FETCH m.content c " +
            "LEFT JOIN FETCH c.notice " +
            "LEFT JOIN FETCH m.pub " +
            "LEFT JOIN FETCH m.notice " +
            "ORDER BY m.category, m.name")
    List<Marker> findAllWithJoins();
}
