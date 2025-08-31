package likelion.festival.service;

import likelion.festival.domain.Artist;
import likelion.festival.dto.ArtistResponseDto;
import likelion.festival.exception.ApiException;
import likelion.festival.exception.ErrorCode;
import likelion.festival.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 가수 비즈니스 로직 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;

    /**
     * 특정 가수 정보 조회
     */
    public ArtistResponseDto getArtistById(Long id) {
        Artist artist = artistRepository.findByIdWithSongs(id)
                .orElseThrow(() -> new ApiException(ErrorCode.ARTIST_NOT_FOUND, "가수 ID: " + id));
        return ArtistResponseDto.from(artist);
    }
}
