package likelion.festival.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class UserFortune {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false)
    private String userKey;

    @Column(nullable = false)
    private LocalDate fortuneDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fortune_id")
    private Fortune fortune;

    @Column(nullable = false)
    private LocalDateTime assignedAt;
}
