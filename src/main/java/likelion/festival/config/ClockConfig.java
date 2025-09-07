package likelion.festival.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    @Bean
    public Clock systemClock() {
        // 서버의 기본 시간대와 상관없이 항상 한국 시간대를 사용하도록 명시
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
