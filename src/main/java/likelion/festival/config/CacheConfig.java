package likelion.festival.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * 캐시 설정 클래스
 */
@Configuration
public class CacheConfig {

    /**
     * 캐시 매니저 빈 설정
     */
    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = Arrays.asList(
                createCache("markers", Duration.ofHours(1)),
                createCache("notices", Duration.ofHours(12))
        );

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    private CaffeineCache createCache(String name, Duration duration) {
        return new CaffeineCache(
                name,
                Caffeine.newBuilder()
                        .expireAfterWrite(duration)
                        .maximumSize(100)
                        .recordStats()
                        .build()
        );
    }
}
