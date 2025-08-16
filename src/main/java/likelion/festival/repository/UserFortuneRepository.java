package likelion.festival.repository;

import likelion.festival.domain.UserFortune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFortuneRepository extends JpaRepository<UserFortune, String> {
}
