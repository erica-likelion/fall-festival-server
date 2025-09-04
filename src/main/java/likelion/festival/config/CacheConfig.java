package likelion.festival.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        var markers = new CaffeineCache(
                "markers",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofHours(1))
                        .maximumSize(100)
                        .recordStats()
                        .build()
        );

        var notices = new CaffeineCache(
                "notices",
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofHours(12))
                        .maximumSize(100)
                        .recordStats()
                        .build()
        );

        var mgr = new SimpleCacheManager();
        mgr.setCaches(List.of(markers, notices));
        return mgr;
    }
}
