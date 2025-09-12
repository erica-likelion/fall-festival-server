package likelion.festival.controller;

import likelion.festival.exception.ApiSuccess;
import likelion.festival.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 캐시 관리 API 컨트롤러
 */
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    /**
     * 모든 캐시 상태 조회
     */
    @GetMapping
    public ApiSuccess<Map<String, Object>> getAllCacheStatus() {
        Map<String, Object> status = cacheService.getAllCacheStatus();
        return ApiSuccess.of(status, "캐시 상태 조회 완료");
    }

    /**
     * 모든 캐시 삭제
     */
    @DeleteMapping
    public ApiSuccess<Map<String, String>> clearAllCache() {
        Map<String, String> result = cacheService.clearAllCache();
        return ApiSuccess.of(result, "캐시 삭제 완료");
    }
}
