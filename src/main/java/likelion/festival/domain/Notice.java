package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "notice_images" , joinColumns = @JoinColumn(name = "notice_id")) // 테이블 안에 리스트 컬럽을 직접 저장 불가해서 이런 방식 사용
    @Column(name = "image_url", nullable = false, length = 500) // 어차피 인스타 글 따올거니까 무조건 사진 있을 예정. nullable = false로 수정
    private List<String> images;

    @Column(nullable = false, length = 50)
    private String tag;
}
