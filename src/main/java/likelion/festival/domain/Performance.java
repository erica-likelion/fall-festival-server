package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공연 엔티티
 * 축제 공연 정보를 관리하는 도메인 객체
 */
@Getter
@Entity
@Table(name = "performances")
public class Performance {

    /**
     * 공연 일차 열거형
     */
    @Getter
    public enum Day {
        FIRST("1일차"),
        SECOND("2일차"),
        THIRD("3일차");

        private final String value;

        Day(String value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;
}
