package likelion.festival.service;

import likelion.festival.dto.MarkerResponseDto;
import likelion.festival.repository.MarkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 마커 비즈니스 로직 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarkerService {

    private final MarkerRepository markerRepository;

    /**
     * 전체 마커 목록 조회
     */
    public List<MarkerResponseDto> getAllMarkers() {
        return markerRepository.findAllWithJoins()
                .stream()
                .map(MarkerResponseDto::from)
                .toList();
    }
}

