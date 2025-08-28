package likelion.festival.fortune.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class FortuneRequestDto {

    private String name;
    private String birth;

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }
}
