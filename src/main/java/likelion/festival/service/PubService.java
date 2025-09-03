package likelion.festival.service;

import likelion.festival.domain.Pub;
import likelion.festival.dto.MenuResponseDto;
import likelion.festival.dto.PubResponseDtos.PubDetailDto;
import likelion.festival.dto.PubResponseDtos.PubSummaryDto;
import likelion.festival.exception.ApiException;
import likelion.festival.exception.ErrorCode;
import likelion.festival.repository.MenuRepository;
import likelion.festival.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 주점 비즈니스 로직 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PubService {

    private final PubRepository pubRepository;
    private final MenuRepository menuRepository;

    /**
     * 전체 주점 목록 조회
     */
    public List<PubSummaryDto> getAllPubs() {
        return pubRepository.findAll()
                .stream()
                .map(PubSummaryDto::from)
                .toList();
    }

    /**
     * 특정 주점 정보 조회
     */
    public PubDetailDto getPubById(Long id) {
        Pub pub = pubRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PUB_NOT_FOUND, "주점 ID: " + id));
        return PubDetailDto.from(pub);
    }

    /**
     * 특정 주점의 메뉴 목록 조회
     */
    public List<MenuResponseDto> getMenusByPubId(Long pubId) {
        if (!pubRepository.existsById(pubId)) {
            throw new ApiException(ErrorCode.PUB_NOT_FOUND, "주점 ID: " + pubId);
        }

        return menuRepository.findByPubId(pubId)
                .stream()
                .map(MenuResponseDto::from)
                .toList();
    }
}
