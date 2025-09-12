package likelion.festival.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 캐시 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;
    private final Map<String, Instant> lastUpdateTimestamps = new ConcurrentHashMap<>();
    private static final DateTimeFormatter KST_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
            .withZone(ZoneId.of("Asia/Seoul"));

    private static final String MARKERS_CACHE = "markers";
    private static final String NOTICES_CACHE = "notices";

    /**
     * 특정 캐시의 갱신 시각을 현재 시간으로 기록
     */
    public void recordUpdate(String cacheName) {
        lastUpdateTimestamps.put(cacheName, Instant.now());
    }

    /**
     * 모든 캐시 상태 조회
     */
    public Map<String, Object> getAllCacheStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put(MARKERS_CACHE, getCacheStats(MARKERS_CACHE));
        result.put(NOTICES_CACHE, getCacheStats(NOTICES_CACHE));
        return result;
    }

    /**
     * 모든 캐시 삭제
     */
    public Map<String, String> clearAllCache() {
        Map<String, String> result = new HashMap<>();
        clearCache(MARKERS_CACHE);
        result.put(MARKERS_CACHE, "cleared");
        clearCache(NOTICES_CACHE);
        result.put(NOTICES_CACHE, "cleared");
        return result;
    }

    /**
     * 특정 캐시 전체 삭제
     */
    private void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 캐시 통계 정보 반환
     */
    private Map<String, Object> getCacheStats(String cacheName) {
        Map<String, Object> stats = new HashMap<>();
        Optional.ofNullable(lastUpdateTimestamps.get(cacheName))
                .ifPresent(time -> stats.put("lastUpdatedAt", KST_FORMATTER.format(time)));

        Cache cache = cacheManager.getCache(cacheName);
        if (cache instanceof CaffeineCache caffeineCache) {
            var nativeCache = caffeineCache.getNativeCache();
            var cacheStats = nativeCache.stats();

            stats.put("name", cacheName);
            stats.put("size", nativeCache.estimatedSize());
            stats.put("hitCount", cacheStats.hitCount());
            stats.put("missCount", cacheStats.missCount());
            stats.put("hitRate", String.format("%.2f%%", cacheStats.hitRate() * 100));
        } else {
            stats.put("name", cacheName);
            stats.put("status", cache != null ? "found but not a Caffeine cache" : "not found");
        }
        return stats;
    }
}
