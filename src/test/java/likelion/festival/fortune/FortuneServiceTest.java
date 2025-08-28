
package likelion.festival.fortune;

import likelion.festival.domain.Fortune;
import likelion.festival.domain.UserDailyFortune;
import likelion.festival.dto.FortuneRequestDto;
import likelion.festival.dto.FortuneResponseDto;
import likelion.festival.repository.FortuneRepository;
import likelion.festival.repository.UserDailyFortuneRepository;
import likelion.festival.service.FortuneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FortuneServiceTest {

    @InjectMocks
    private FortuneService fortuneService;

    @Mock
    private FortuneRepository fortuneRepository;

    @Mock
    private UserDailyFortuneRepository userDailyFortuneRepository;

    @Mock
    private Clock clock;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private FortuneRequestDto requestDto;
    private String userKey;

    @BeforeEach
    void setUp() {
        requestDto = new FortuneRequestDto();
        requestDto.setName("테스트");
        requestDto.setBirth("19990101");
        // Service 로직과 동일하게 userKey 생성
        userKey = "테스트19990101";
    }

    private void setClock(LocalDate date) {
        Instant instant = date.atStartOfDay(KST).toInstant();
        lenient().when(clock.withZone(KST)).thenReturn(Clock.fixed(instant, KST));
        lenient().when(clock.getZone()).thenReturn(KST);
        lenient().when(clock.instant()).thenReturn(instant);
    }

    @Test
    @DisplayName("축제 3일간 운세 조회 시나리오 테스트")
    void threeDayFortuneScenario() {
        // --- 준비 (Given) ---
        Fortune f1 = new Fortune(); f1.setId(1L); f1.setImageUrl("url1");
        Fortune f2 = new Fortune(); f2.setId(2L); f2.setImageUrl("url2");
        Fortune f3 = new Fortune(); f3.setId(3L); f3.setImageUrl("url3");
        List<Fortune> allFortunes = List.of(f1, f2, f3);
        when(fortuneRepository.findAll()).thenReturn(allFortunes);

        // --- 1일차 (When & Then) ---
        LocalDate day1 = LocalDate.of(2024, 9, 26);
        setClock(day1);

        // 1일차 첫 요청: DB에 기록 없음
        // save 후 재조회 시 반환될 객체를 미리 정의
        UserDailyFortune udf1 = new UserDailyFortune();
        udf1.setUserKey(userKey);
        udf1.setFortuneDate(day1);
        // 로직상 f1, f2, f3 중 하나가 랜덤 선택됨. 테스트에서는 f1이 선택되었다고 가정
        udf1.setFortune(f1);
        when(userDailyFortuneRepository.findByUserKeyAndFortuneDate(userKey, day1))
                .thenReturn(Optional.empty()) // 첫 조회
                .thenReturn(Optional.of(udf1)); // 저장 후 조회 및 재요청 시

        // 첫 요청 실행
        FortuneResponseDto response1 = fortuneService.getTodayFortune(requestDto);
        assertThat(response1).isNotNull();
        String day1ImageUrl = response1.getImageUrl();
        System.out.println("1일차 운세: " + day1ImageUrl);

        // 1일차 재요청: DB에 기록이 있으므로 동일한 결과 반환
        FortuneResponseDto response1Repeat = fortuneService.getTodayFortune(requestDto);
        assertThat(response1Repeat.getImageUrl()).isEqualTo(day1ImageUrl);
        System.out.println("1일차 재요청 운세: " + response1Repeat.getImageUrl());


        // --- 2일차 (When & Then) ---
        LocalDate day2 = LocalDate.of(2024, 9, 27);
        setClock(day2);

        // 2일차 첫 요청: DB에 기록 없음
        UserDailyFortune udf2 = new UserDailyFortune();
        udf2.setUserKey(userKey);
        udf2.setFortuneDate(day2);
        // 1일차 운세(f1)를 제외한 f2, f3 중 f2가 선택되었다고 가정
        udf2.setFortune(f2);
        when(userDailyFortuneRepository.findByUserKeyAndFortuneDate(userKey, day2))
                .thenReturn(Optional.empty()) // 첫 조회
                .thenReturn(Optional.of(udf2)); // 저장 후 조회
        // 1일차에 사용한 운세 ID 목록 반환
        when(userDailyFortuneRepository.findUsedFortuneIdsInRange(eq(userKey), any(LocalDate.class), eq(day2))).thenReturn(List.of(f1.getId()));

        FortuneResponseDto response2 = fortuneService.getTodayFortune(requestDto);
        assertThat(response2).isNotNull();
        String day2ImageUrl = response2.getImageUrl();
        System.out.println("2일차 운세: " + day2ImageUrl);
        assertThat(day2ImageUrl).isNotEqualTo(day1ImageUrl);


        // --- 3일차 (When & Then) ---
        LocalDate day3 = LocalDate.of(2024, 9, 28);
        setClock(day3);

        // 3일차 첫 요청: DB에 기록 없음
        UserDailyFortune udf3 = new UserDailyFortune();
        udf3.setUserKey(userKey);
        udf3.setFortuneDate(day3);
        // 1, 2일차 운세(f1, f2)를 제외한 f3가 선택되어야 함
        udf3.setFortune(f3);
        when(userDailyFortuneRepository.findByUserKeyAndFortuneDate(userKey, day3))
                .thenReturn(Optional.empty()) // 첫 조회
                .thenReturn(Optional.of(udf3)); // 저장 후 조회
        // 1, 2일차에 사용한 운세 ID 목록 반환
        when(userDailyFortuneRepository.findUsedFortuneIdsInRange(eq(userKey), any(LocalDate.class), eq(day3))).thenReturn(List.of(f1.getId(), f2.getId()));

        FortuneResponseDto response3 = fortuneService.getTodayFortune(requestDto);
        assertThat(response3).isNotNull();
        String day3ImageUrl = response3.getImageUrl();
        System.out.println("3일차 운세: " + day3ImageUrl);
        assertThat(day3ImageUrl).isNotEqualTo(day1ImageUrl);
        assertThat(day3ImageUrl).isNotEqualTo(day2ImageUrl);
    }
}
