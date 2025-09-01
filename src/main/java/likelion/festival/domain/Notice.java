package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor // jpa때문에 필요
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500) // 공지에 사진이 없는 경우가 있을까봐 혹시 몰라서 일단 nullable = true로 했습니다.
    private String image;

    @Column(nullable = false, length = 50)
    private String tag;
}
