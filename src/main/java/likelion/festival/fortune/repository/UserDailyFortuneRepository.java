package likelion.festival.fortune.repository;

import likelion.festival.fortune.domain.UserDailyFortune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDailyFortuneRepository extends JpaRepository<UserDailyFortune, Long> {

    // 같은 날 이미 배정된 운세가 있으면 그대로 쓰기
    Optional<UserDailyFortune> findByUserKeyAndFortuneDate(String userKey, LocalDate fortuneDate);

    // 최근 N일(예: today-2 .. today) 동안 이 유저가 받은 fortune_id 목록
    @Query("""
           select udf.fortune.id
             from UserDailyFortune udf
            where udf.userKey = :userKey
              and udf.fortuneDate between :start and :end
           """)
    List<Long> findUsedFortuneIdsInRange(String userKey, LocalDate start, LocalDate end);
}

