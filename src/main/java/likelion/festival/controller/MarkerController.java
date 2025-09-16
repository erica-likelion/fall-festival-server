package likelion.festival.controller;

import likelion.festival.dto.MarkerResponseDto;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.service.MarkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 마커 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/markers")
@RequiredArgsConstructor
public class MarkerController {

    private final MarkerService markerService;

    /**
     * 전체 마커 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiSuccess<List<MarkerResponseDto>>> getAllMarkers() {
        List<MarkerResponseDto> markers = markerService.getAllMarkers();

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .body(ApiSuccess.of(markers, "마커 목록 조회 완료"));
    }
}

