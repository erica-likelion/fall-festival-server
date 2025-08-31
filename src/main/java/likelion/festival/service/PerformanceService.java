package likelion.festival.service;

import likelion.festival.dto.PerformanceResponseDto;
import likelion.festival.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 공연 비즈니스 로직 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    /**
     * 전체 공연 목록 조회
     */
    public List<PerformanceResponseDto> getAllPerformances() {
        return performanceRepository.findAllWithArtist()
                .stream()
                .map(PerformanceResponseDto::from)
                .toList();
    }
}
