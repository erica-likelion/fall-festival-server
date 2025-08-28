package likelion.festival.fortune;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.fortune.domain.Fortune;
import likelion.festival.fortune.dto.FortuneResponseDto;
import likelion.festival.fortune.repository.FortuneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FortuneControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FortuneRepository fortuneRepository;
    @Autowired
    private Clock clock; // MutableClock 주입

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
    @DisplayName("운세 조회 시나리오: 같은 날은 같은 운세, 다른 날은 최근 3일간 사용하지 않은 새로운 운세")
    void fortuneScenario() throws Exception {
        // given: 공통 요청자 정보
        Map<String, String> request = Map.of("name", "하냥이", "birth", "20021231");

        // when: 9/15 10:00 첫 요청
        setKst(2025, 9, 15, 10);
        String url1 = postAndGetUrl(request, 14 * 3600);
        System.out.println("9/15 10:00 -> " + url1);

        // then: 9/15 15:00 재요청 -> 같은 운세 반환
        setKst(2025, 9, 15, 15);
        String url1Again = postAndGetUrl(request, 9 * 3600);
        System.out.println("9/15 15:00 -> " + url1Again);
        assertThat(url1Again).isEqualTo(url1);

        // when: 9/16 10:00 다음날 요청
        setKst(2025, 9, 16, 10);
        String url2 = postAndGetUrl(request, 14 * 3600);
        System.out.println("9/16 10:00 -> " + url2);

        // then: 다른 운세 반환
        assertThat(url2).isNotEqualTo(url1);

        // then: 9/16 15:00 재요청 -> 같은 운세 반환
        setKst(2025, 9, 16, 15);
        String url2Again = postAndGetUrl(request, 9 * 3600);
        System.out.println("9/16 15:00 -> " + url2Again);
        assertThat(url2Again).isEqualTo(url2);

        // when: 9/17 10:00 다다음날 요청
        setKst(2025, 9, 17, 10);
        String url3 = postAndGetUrl(request, 14 * 3600);
        System.out.println("9/17 10:00 -> " + url3);

        // then: 이전과 모두 다른 운세 반환
        assertThat(url3).isNotEqualTo(url1).isNotEqualTo(url2);
    }

    private String postAndGetUrl(Map<String, String> requestBody, int expectedMaxAge) throws Exception {
        String json = objectMapper.writeValueAsString(requestBody);
        MvcResult result = mockMvc.perform(post("/api/fortunes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "max-age=" + expectedMaxAge + ", private"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        FortuneResponseDto body = objectMapper.readValue(responseBody, FortuneResponseDto.class);
        return body.getImageUrl();
    }
}