package likelion.festival.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_daily_fortune",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_date",
                columnNames = {"user_key", "fortune_date"}
        ),
        indexes = {
                @Index(name = "idx_user_date", columnList = "user_key, fortune_date"),
                @Index(name = "idx_user_fortune_date", columnList = "user_key, fortune_id, fortune_date")
        }
)
public class UserDailyFortune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // name + birth(예: "이규현|970515") 또는 그 해시
    @Column(name = "user_key", nullable = false, length = 128)
    private String userKey;

    // KST 기준 '오늘' 날짜
    @Column(name = "fortune_date", nullable = false)
    private LocalDate fortuneDate;

    // 배정된 운세 이미지
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fortune_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_udf_fortune"))
    private Fortune fortune;

    // 배정 시각(앱에서 세팅; 원하면 @CreationTimestamp 사용 가능)
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public Long getId() {
        return id;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public LocalDate getFortuneDate() {
        return fortuneDate;
    }

    public void setFortuneDate(LocalDate fortuneDate) {
        this.fortuneDate = fortuneDate;
    }

    public Fortune getFortune() {
        return fortune;
    }

    public void setFortune(Fortune fortune) {
        this.fortune = fortune;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
