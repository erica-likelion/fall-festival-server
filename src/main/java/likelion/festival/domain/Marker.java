package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 마커 엔티티
 * 지도에 표시될 마커 정보를 관리하는 도메인 객체
 */
@Getter
@Entity
@Table(name = "markers")
public class Marker {

    /**
     * 마커 카테고리 열거형
     */
    @Getter
    @AllArgsConstructor
    public enum Category {
        PUB("주점"),
        EVENT("이벤트"),
        FOOD_TRUCK("푸드트럭"),
        CONTENT("콘텐츠"),
        TOILET("화장실"),
        MEDICAL_ROOM("의무실"),
        SHUTTLE_COCK("셔틀콕"),
        PERFORMANCE_HALL("공연장"),
        SMOKING_AREA("흡연실"),
        ALCOHOL_PURCHASE("주류 구매"),
        FLEA_MARKET("플리마켓");

        private final String value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 500)
    private String image;

    @Column(nullable = false, length = 50)
    private String name;

    // 연결 정보가 없는 정적 마커용 위경도 (nullable)
    private Double latitude;
    private Double longitude;

    @Column(nullable = false, length = 50)
    private String time;

    @ElementCollection
    @CollectionTable(name = "marker_closed_days", joinColumns = @JoinColumn(name = "marker_id"))
    @Column(name = "closed_day")
    private List<String> closedDays;

    // 연결 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // 실제 위경도 반환 메서드 (연결된 Entity 참조)
    public Double getActualLatitude() {
        return hasEventLink() ? event.getLatitude()
                : hasPubLink() ? pub.getLatitude()
                : latitude;
    }

    public Double getActualLongitude() {
        return hasEventLink() ? event.getLongitude()
                : hasPubLink() ? pub.getLongitude()
                : longitude;
    }

    // 연결 정보 확인 메서드들
    public boolean hasEventLink() {
        return this.event != null;
    }

    public boolean hasPubLink() {
        return this.pub != null;
    }

    public boolean hasNoticeLink() {
        return this.notice != null;
    }
}
