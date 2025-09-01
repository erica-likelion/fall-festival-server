package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 대표곡 엔티티
 * 가수의 대표곡 정보를 관리하는 도메인 객체
 */
@Getter
@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String link;

    @Column(length = 500, nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;
}
