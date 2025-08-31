package likelion.festival.fortune;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.domain.Fortune;
import likelion.festival.repository.FortuneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FortuneControllerPerformanceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FortuneRepository fortuneRepository;
    @Autowired
    private Clock clock;

    @TestConfiguration
    static class TestClockConfig {
        @Bean
        @Primary
        public Clock testClock() {
            // 2025-08-20 00:00 KST 시작
            return new MutableClock(
                    LocalDateTime.of(2025, 8, 20, 0, 0)
                            .atZone(ZoneId.of("Asia/Seoul"))
                            .toInstant(),
                    ZoneId.of("Asia/Seoul")
            );
        }

        static class MutableClock extends Clock {
            private Instant instant;
            private final ZoneId zone;

            MutableClock(Instant initial, ZoneId zone) {
                this.instant = initial;
                this.zone = zone;
            }

            public void setKst(LocalDateTime dateTime) {
                this.instant = dateTime.atZone(zone).toInstant();
            }

            @Override
            public ZoneId getZone() { return zone; }

            @Override
            public Clock withZone(ZoneId zone) { return new MutableClock(instant, zone); }

            @Override
            public Instant instant() { return instant; }
        }
    }

    private void setKst(int year, int month, int day, int hour) {
        if (!(clock instanceof TestClockConfig.MutableClock mc)) {
            throw new IllegalStateException("테스트용 MutableClock이 주입되지 않았습니다.");
        }
        mc.setKst(LocalDateTime.of(year, month, day, hour, 0));
    }

    @BeforeEach
    void seedFortunes() {
        if (fortuneRepository.count() == 0) {
            List<Fortune> list = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                Fortune f = new Fortune();
                f.setImageUrl("https://cdn.example.com/fortune/fortune-%02d.jpg".formatted(i));
                list.add(f);
            }
            fortuneRepository.saveAll(list);
        }
    }

    @Test
    @DisplayName("동일 유저 재요청 시 응답 속도 측정")
    void measureResponseTimeForSameUserRequest() throws Exception {
        // given: 동일한 요청자 정보
        Map<String, String> request = Map.of("name", "측정용", "birth", "19990101");
        String jsonRequest = objectMapper.writeValueAsString(request);
        setKst(2025, 10, 1, 12); // 특정 시간으로 고정

        // when: 첫 번째 요청
        long startTime1 = System.nanoTime();
        mockMvc.perform(post("/api/fortunes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        long endTime1 = System.nanoTime();
        double duration1 = (endTime1 - startTime1) / 1_000_000.0;
        System.out.printf("첫 번째 요청 응답 시간: %.3f ms%n", duration1);

        // when: 두 번째 요청 (동일한 요청)
        long startTime2 = System.nanoTime();
        mockMvc.perform(post("/api/fortunes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        long endTime2 = System.nanoTime();
        double duration2 = (endTime2 - startTime2) / 1_000_000.0;
        System.out.printf("두 번째 요청 응답 시간: %.3f ms%n", duration2);

        assertThat(duration2).isPositive();
    }
}
