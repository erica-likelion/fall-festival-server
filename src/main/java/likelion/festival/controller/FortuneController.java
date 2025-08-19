package likelion.festival.controller;

import likelion.festival.dto.FortuneRequestDto;
import likelion.festival.dto.FortuneResponseDto;
import likelion.festival.service.FortuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("api/fortunes")
@RequiredArgsConstructor
public class FortuneController {

    private final FortuneService fortuneService;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private final Clock clock;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<FortuneResponseDto> getTodayFortune(@RequestBody FortuneRequestDto request){
        FortuneResponseDto response = fortuneService.getTodayFortune(request);

        // 00시까지 캐시
        int seconds = secondsUntilMidnightKST();
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL,
                        CacheControl.maxAge(seconds, TimeUnit.SECONDS)
                                .cachePrivate().getHeaderValue())
                .body(response);
    }

    private int secondsUntilMidnightKST() {
        //ZonedDateTime now = ZonedDateTime.now(KST);
        ZonedDateTime now = ZonedDateTime.now(clock.withZone(KST));//테스트용
        ZonedDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(KST);
        return (int) Duration.between(now, midnight).getSeconds();
    }
}
