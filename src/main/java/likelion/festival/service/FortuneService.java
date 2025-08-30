package likelion.festival.service;

import likelion.festival.domain.Fortune;
import likelion.festival.domain.UserDailyFortune;
import likelion.festival.dto.FortuneRequestDto;
import likelion.festival.dto.FortuneResponseDto;
import likelion.festival.repository.FortuneRepository;
import likelion.festival.repository.UserDailyFortuneRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private final Clock clock;

    public FortuneService(FortuneRepository fortuneRepository, UserDailyFortuneRepository userDailyFortuneRepository, Clock clock) {
        this.fortuneRepository = fortuneRepository;
        this.userDailyFortuneRepository = userDailyFortuneRepository;
        this.clock = clock;
    }

    private static final int FESTIVAL_DAY = 3; // 축제 3일간
    private static final ZoneId KST = ZoneId.of("Asia/Seoul"); // 기준 한국시간으로

    @Transactional
    public FortuneResponseDto getTodayFortune(FortuneRequestDto req) {
        String name = normalizeName(req.getName());
        String birth = requireBirth(req.getBirth());
        String userKey = toSha256(name + birth);

        LocalDate today = LocalDate.now(clock.withZone(KST));

        Optional<UserDailyFortune> existing = userDailyFortuneRepository.findByUserKeyAndFortuneDate(userKey, today);
        if (existing.isPresent()) {
            return fortuneResponse(existing.get().getFortune());
        }

        LocalDate start = today.minusDays(FESTIVAL_DAY - 1);
        List<Long> useFortunes = userDailyFortuneRepository.findUsedFortuneIdsInRange(userKey, start, today);

        List<Fortune> fortunes = fortuneRepository.findAll();
        List<Fortune> filteredFortunes = fortunes.stream()
                .filter(f -> !useFortunes.contains(f.getId()))
                .collect(Collectors.toList());

        Fortune pick = pickRandomFortune(filteredFortunes);

        try {
            UserDailyFortune userDailyFortune = new UserDailyFortune();
            userDailyFortune.setUserKey(userKey);
            userDailyFortune.setFortuneDate(today);
            userDailyFortune.setFortune(pick);
            userDailyFortune.setAssignedAt(LocalDateTime.now(clock.withZone(KST)));

            userDailyFortuneRepository.saveAndFlush(userDailyFortune);
        } catch (DataIntegrityViolationException race) {
            // 동시성 대비: 동시 요청이 겹쳤을 때 -> 재조회 해서 동일한 결과 반환하게
        }

        UserDailyFortune saved = userDailyFortuneRepository
                .findByUserKeyAndFortuneDate(userKey, today)
                .orElseThrow(() -> new IllegalStateException("Failed to save or retrieve daily fortune."));

        return fortuneResponse(saved.getFortune());
    }

    private String toSha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 해싱 실패
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private String normalizeName(String name) {
        if (name == null) throw new IllegalArgumentException("이름 필수");
        String trimmed = name.trim().replaceAll("\s+", " ");
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
            throw new IllegalArgumentException("birth는 yyyyMMdd 형식이어야 합니다.");
        }
        return birth;
    }

    private static FortuneResponseDto fortuneResponse(Fortune fortune) {
        FortuneResponseDto dto = new FortuneResponseDto();
        dto.setImageUrl(fortune.getImageUrl());
        return dto;
    }
}
