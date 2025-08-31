package likelion.festival.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    @Bean
    @Profile("!test") // test 프로파일이 아닐 때 사용
    public Clock systemClock() {
        // 서버의 기본 시간대와 상관없이 항상 한국 시간대를 사용하도록 명시
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }

    @Bean
    @Profile("test") // test 프로파일일 때 사용
    public Clock testClock() {
        // 테스트 환경에서는 고정된 시간을 반환하는 Clock을 사용
        // (주의: 이 방식은 통합 테스트(@SpringBootTest)에서 유용
        return Clock.systemDefaultZone(); // 기본값은 시스템 시간, 테스트별로 Mock으로 대체 가능
    }
}
