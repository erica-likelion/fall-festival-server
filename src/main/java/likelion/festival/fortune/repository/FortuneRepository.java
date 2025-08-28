package likelion.festival.fortune.repository;

import likelion.festival.fortune.domain.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
}
