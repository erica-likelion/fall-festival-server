package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 주점 엔티티
 * 축제 주점 정보를 관리하는 도메인 객체
 */
@Getter
@Entity
@Table(name = "pubs")
public class Pub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String location;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(length = 50, nullable = false)
    private String affiliation;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean takeout;

    @Column(name = "profile_image", length = 500, nullable = false)
    private String profileImage;

    @Column(name = "poster_image", length = 500, nullable = false)
    private String posterImage;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();
}
