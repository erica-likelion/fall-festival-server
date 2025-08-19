package likelion.festival.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AppConfig {

    @Bean
    public Clock clock(){
        //한국 기준 시스템 시간
        return Clock.systemDefaultZone();
    }
}
