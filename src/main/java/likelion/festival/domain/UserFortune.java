package likelion.festival.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class UserFortune {

    /**
     * 이름+생년월일+요청day(9월13일이면 13)
     * ex: 크리스티아누호날두2001040113
     * 맨 뒤 숫자로 day 식별
     */
    @Id
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fortune_id", nullable = false)
    private Fortune fortune;

    public Fortune getFortune() {
        return fortune;
    }

    public void setFortune(Fortune fortune) {
        this.fortune = fortune;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
