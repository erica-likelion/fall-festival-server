package likelion.festival.repository;

import likelion.festival.domain.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
    Fortune findFortuneById(Long randomFortuneId);
}
