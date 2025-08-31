package likelion.festival.controller;

import likelion.festival.dto.ArtistResponseDto;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 가수 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    /**
     * 특정 가수 정보 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccess<ArtistResponseDto>> getArtistById(@PathVariable Long id) {
        ArtistResponseDto artist = artistService.getArtistById(id);
        return ResponseEntity.ok(ApiSuccess.of(artist, "가수 정보 조회 완료"));
    }
}
