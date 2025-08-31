package likelion.festival.repository;

import likelion.festival.domain.Pub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 주점 데이터 접근 레포지토리
 */
@Repository
public interface PubRepository extends JpaRepository<Pub, Long> {
}
