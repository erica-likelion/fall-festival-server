package likelion.festival.service;

import likelion.festival.domain.Fortune;
import likelion.festival.domain.UserFortune;
import likelion.festival.dto.FortuneResponseDto;
import likelion.festival.repository.FortuneRepository;
import likelion.festival.repository.UserFortuneRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;

@Service
public class FortuneService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final FortuneRepository fortuneRepository;
    private final UserFortuneRepository userFortuneRepository;

    public FortuneService(FortuneRepository fortuneRepository, UserFortuneRepository userFortuneRepository) {
        this.fortuneRepository = fortuneRepository;
        this.userFortuneRepository = userFortuneRepository;
    }

    /**
     * 요청 받아서 오늘 운세를 반환(오늘 거 있으면 있는 거 반환, 없으면 생성)
     */
    @Transactional
    public FortuneResponseDto getOrCreate(String name, String birth){
        //pk: 이름+생년월일+day(2자리)
        LocalDate now = LocalDate.now(KST);
        String pk = createdFortuneId(name, birth, now.getDayOfMonth());

        return userFortuneRepository.findById(pk)
                .map(uf -> toResponse(uf.getFortune()))
                .orElseGet(() -> matchTodayFortune(pk));
    }

    //랜덤 매핑
    private FortuneResponseDto matchTodayFortune(String pk){
        Random random = new Random();
        Long randomFortuneId = random.nextLong(1, 41);
        Fortune picked = fortuneRepository.findFortuneById(randomFortuneId);

        try{
            UserFortune uf = new UserFortune();
            uf.setId(pk);
            uf.setFortune(picked);
            userFortuneRepository.saveAndFlush(uf);
            return toResponse(picked);
        }catch (DataIntegrityViolationException dup){
            //이미 누군가 같은 PK로 저장했다면 -> 그 값 응답
            return userFortuneRepository.findById(pk)
                    .map(uf -> toResponse(uf.getFortune()))
                    .orElseThrow(() -> dup);
        }
    }

    //ID 생성 이름+생년월일+day
    public String createdFortuneId(String name, String birth, int day){
        //ex: 2002-02-01 -> 20020201
        String cleanBirth = birth.replaceAll("-", "");
        //ex: 9일 들어오면 -> 09일로
        String dayToStr = String.format("%02d", day);
        return name+cleanBirth+dayToStr;
    }

    private FortuneResponseDto toResponse(Fortune f){
        FortuneResponseDto dto = new FortuneResponseDto();
        dto.setMessage(f.getMessage());
        dto.setColor(f.getColor());
        dto.setScore(f.getScore());
        dto.setImage_url(f.getImageUrl());
        return dto;
    }

}
