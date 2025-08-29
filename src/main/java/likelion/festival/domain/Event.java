package likelion.festival.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 축제 이벤트
 * 시간별로 이벤트 보내주기
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String period;

    private String place; //이벤트 장소

    @Builder
    public Event(String title, LocalDateTime startTime, LocalDateTime endTime, String period, String place) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.period = period;
        this.place = place;
    }
}
