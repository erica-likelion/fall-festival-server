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
        CONTENT("콘텐츠"),
        FOOD_TRUCK("푸드트럭"),
        PROMOTION("프로모션"),
        TOILET("화장실"),
        MEDICAL_ROOM("의무실"),
        SHUTTLE_COCK("셔틀콕"),
        PERFORMANCE_HALL("공연장"),
        SMOKING_AREA("흡연구역"),
        ALCOHOL_PURCHASE("주류 구매"),
        FLEA_MARKET("플리마켓"),
        AED("AED");

        private final String value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(length = 500)
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
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // 실제 위경도 반환 메서드 (연결된 Entity 참조)
    public Double getActualLatitude() {
        return hasContentLink() ? content.getLatitude()
                : hasPubLink() ? pub.getLatitude()
                : latitude;
    }

    public Double getActualLongitude() {
        return hasContentLink() ? content.getLongitude()
                : hasPubLink() ? pub.getLongitude()
                : longitude;
    }

    // 실제 이미지 반환 메서드 (연결된 Entity 참조)
    public String getActualImage() {
        return hasContentLink() && content.getNotice() != null ? content.getNotice().getImages().get(0)
                : hasPubLink() ? pub.getProfileImage()
                : hasNoticeLink() ? notice.getImages().get(0)
                : image;
    }

    // 연결 정보 확인 메서드들
    public boolean hasContentLink() {
        return this.content != null;
    }

    public boolean hasPubLink() {
        return this.pub != null;
    }

    public boolean hasNoticeLink() {
        return this.notice != null;
    }
}
