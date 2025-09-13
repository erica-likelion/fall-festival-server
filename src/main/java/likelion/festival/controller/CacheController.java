package likelion.festival.controller;

import likelion.festival.exception.ApiException;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.exception.ErrorCode;
import likelion.festival.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 캐시 관리 API 컨트롤러
 */
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @Value("${admin.key}")
    private String adminKey;

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
    public ApiSuccess<Map<String, String>> clearAllCache(@RequestParam(value = "key", required = false) String key) {
        if (key == null || !key.equals(adminKey)) {
            throw new ApiException(ErrorCode.INVALID_ADMIN_KEY);
        }
        Map<String, String> result = cacheService.clearAllCache();
        return ApiSuccess.of(result, "캐시 삭제 완료");
    }
}
