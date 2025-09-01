package likelion.festival.domain;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false, length = 50)
    private String period;

    @Column(nullable = false, length = 50)
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Builder
    public Event(String title, LocalDateTime startTime, LocalDateTime endTime, String period, String place, Notice notice) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.period = period;
        this.place = place;
        this.notice = notice;
    }
}
