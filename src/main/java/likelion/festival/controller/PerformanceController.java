package likelion.festival.controller;

import likelion.festival.dto.PerformanceResponseDto;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 공연 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    /**
     * 전체 공연 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiSuccess<List<PerformanceResponseDto>>> getAllPerformances() {
        List<PerformanceResponseDto> performances = performanceService.getAllPerformances();
        return ResponseEntity.ok(ApiSuccess.of(performances, "공연 목록 조회 완료"));
    }
}
