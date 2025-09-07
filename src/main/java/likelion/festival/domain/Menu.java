package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 메뉴 엔티티
 * 주점의 메뉴 정보를 관리하는 도메인 객체
 */
@Getter
@Entity
@Table(name = "menus")
public class Menu {

    /**
     * 메뉴 카테고리 열거형
     */
    public enum Category {
        main, side, set, others
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(columnDefinition = "TEXT") // category == others인 경우 null
    private String description;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", nullable = false)
    private Pub pub;
}
