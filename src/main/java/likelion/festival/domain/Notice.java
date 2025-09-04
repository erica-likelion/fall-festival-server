package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

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

    // 이미지url들 json 형식으로
    @Type(JsonType.class)
    @Column(columnDefinition = "json", nullable = false)
    private List<String> images;

    @Column(nullable = false, length = 50)
    private String tag;
}
