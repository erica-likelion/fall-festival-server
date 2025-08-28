package likelion.festival.fortune.service;

import likelion.festival.fortune.domain.Fortune;
import likelion.festival.fortune.domain.UserDailyFortune;
import likelion.festival.fortune.dto.FortuneRequestDto;
import likelion.festival.fortune.dto.FortuneResponseDto;
import likelion.festival.fortune.repository.FortuneRepository;
import likelion.festival.fortune.repository.UserDailyFortuneRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class FortuneService {

    private final FortuneRepository fortuneRepository;
    private final UserDailyFortuneRepository userDailyFortuneRepository;
    //테스트 할때 시간조작하려고 하는거
    private final Clock clock;//실제 서버는 주석처리

    public FortuneService(FortuneRepository fortuneRepository, UserDailyFortuneRepository userDailyFortuneRepository, Clock clock) {
        this.fortuneRepository = fortuneRepository;
        this.userDailyFortuneRepository = userDailyFortuneRepository;
        this.clock = clock;
    }

    private static final int FESTIVAL_DAY = 3; //축제 3일간
    private static final ZoneId KST = ZoneId.of("Asia/Seoul"); //기준 한국시간으로

    @Transactional
    public FortuneResponseDto getTodayFortune(FortuneRequestDto req){
        String name = normalizeName(req.getName()); //이름 받은 거 데이터 처리(ex: 공백 등)
        String birth = requireBirth(req.getBirth());//20250820 형식
        String userKey = name + birth;

        /**
         * 로컬 서버에서 테스트할 때(clock) today
         * 실제 LocalDate today
         */
        //LocalDate today = LocalDate.now(KST);//실제
        LocalDate today = LocalDate.now(clock.withZone(KST));//로컬환경 테스트

        //요청 받은 날(오늘) 매핑 체크
        Optional<UserDailyFortune> existing = userDailyFortuneRepository.findByUserKeyAndFortuneDate(userKey, today);
        if(existing.isPresent()){
            return fortuneResponse(existing.get().getFortune());//운세 조회 했으면 그대로 반환
        }
        //없으면 운세 아이디 체크해서 매핑 안했던 거로 골라줘야함
        LocalDate start = today.minusDays(FESTIVAL_DAY-1);
        List<Long> useFortunes = userDailyFortuneRepository.findUsedFortuneIdsInRange(userKey, start, today);

        //전체 운세에서 위에 사용했던 운세 빼
        List<Fortune> fortunes  = fortuneRepository.findAll();
        List<Fortune> filteredFortunes = fortunes.stream()
                .filter(f -> !useFortunes.contains(f.getId()))
                .collect(Collectors.toList());

        Fortune pick = pickRandomFortune(filteredFortunes);

        //유니크 조건 예외
        try{
            UserDailyFortune userDailyFortune = new UserDailyFortune();
            userDailyFortune.setUserKey(userKey);
            userDailyFortune.setFortuneDate(today);
            userDailyFortune.setFortune(pick);
            /**
             * 테스트할 때 clock 사용
             */
            userDailyFortune.setAssignedAt(LocalDateTime.now(clock.withZone(KST)));//테스트
            //userDailyFortune.setAssignedAt(LocalDateTime.now(KST));//실제

            userDailyFortuneRepository.saveAndFlush(userDailyFortune);
        }catch (DataIntegrityViolationException race){
            //동시성 대비. 동시 요청이 겹쳤을 때 -> 재조회 해서 동일한 결과 반환하게
        }

        UserDailyFortune saved = userDailyFortuneRepository
                .findByUserKeyAndFortuneDate(userKey, today)
                .orElseThrow();

        return fortuneResponse(saved.getFortune());
    }

    private String normalizeName(String name) {
        if(name == null) throw new IllegalArgumentException("이름 필수");
        String trimmed = name.trim().replaceAll("\\s+", " ");
        return java.text.Normalizer.normalize(trimmed, java.text.Normalizer.Form.NFKC);
    }

    private static Fortune pickRandomFortune(List<Fortune> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("등록된 운세 이미지가 없습니다.");
        }
        int idx = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(idx);
    }

    private static String requireBirth(String birth) {
        if (birth == null || !birth.matches("\\d{8}")) {
            // 필요하면 6자리(yyMMdd) 허용으로 변경 가능:  birth.matches("\\d{6}|\\d{8}")
            throw new IllegalArgumentException("birth는 yyyyMMdd 형식이어야 합니다.");
        }
        return birth;
    }

    private static FortuneResponseDto fortuneResponse(Fortune fortune){
        FortuneResponseDto dto = new FortuneResponseDto();
        dto.setImageUrl(fortune.getImageUrl());
        return dto;
    }


}
