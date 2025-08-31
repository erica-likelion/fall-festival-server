package likelion.festival.repository;

import likelion.festival.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 메뉴 데이터 접근 레포지토리
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * 특정 주점의 모든 메뉴 조회
     */
    List<Menu> findByPubId(Long pubId);
}
