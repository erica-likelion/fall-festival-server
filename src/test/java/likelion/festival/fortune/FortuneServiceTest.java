package likelion.festival.fortune;

import likelion.festival.domain.Fortune;
import likelion.festival.domain.UserFortune;
import likelion.festival.dto.FortuneResponseDto;
import likelion.festival.repository.FortuneRepository;
import likelion.festival.repository.UserFortuneRepository;
import likelion.festival.service.FortuneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FortuneServiceTest {

    @Autowired
    private FortuneRepository fortuneRepo;

    @Autowired
    private UserFortuneRepository userRepo;

    @Autowired
    private FortuneService fortuneService;

    private final String name = "하냥이";
    private final String birth = "2025-08-17";

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
    }

    @Test
    @DisplayName("같은 날 요청 시 해당 운세 반환")
    void testSameDayReturnsSameFortune() {
        FortuneResponseDto first = fortuneService.getOrCreate(name, birth);
        FortuneResponseDto second = fortuneService.getOrCreate(name, birth);

        System.out.println(first.getMessage()+"\n"+first.getScore()+" "+first.getScore()+"\n"+first.getColor());
        System.out.println(second.getMessage()+"\n"+second.getScore()+" "+second.getScore()+"\n"+second.getColor());

        assertThat(first.getMessage()).isEqualTo(second.getMessage());
        assertThat(first.getScore()).isEqualTo(second.getScore());
    }

    @Test
    @DisplayName("다음 날 요청하면 새로운 운세가 반환된다 (테스트용 ID 강제 조작)")
    void testDifferentDayReturnsDifferentFortune() {
        // 1. 첫날 요청
        FortuneResponseDto todayFortune = fortuneService.getOrCreate(name, birth);

        // 2. 하루 지난 것처럼 PK 조작 후 직접 저장
        LocalDate tomorrow = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1);
        String nextDayId = fortuneService.createdFortuneId(name, birth, tomorrow.getDayOfMonth());

        // 3. 운세 1개 직접 저장해서 매핑
        List<Fortune> all = fortuneRepo.findAll();
        Fortune forcedFortune = all.get(0); // 아무거나 하나

        UserFortune uf = new UserFortune();
        uf.setId(nextDayId);
        uf.setFortune(forcedFortune);
        userRepo.save(uf);

        // 4. 다시 getOrCreate 호출 → "내일"이 아닌 현재 날짜기 때문에 또 오늘 운세가 반환
        FortuneResponseDto stillToday = fortuneService.getOrCreate(name, birth);

        // 5. 직접 저장한 내일자 운세와 오늘자 운세가 달라야 함
        assertThat(stillToday.getMessage()).isNotEqualTo(forcedFortune.getMessage());
    }

}
