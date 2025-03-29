package com.alibou.security.config.springCache;

import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "movie.cache")
public class CacheProperties {

    private CacheType type;
    private Redis redis;

    @Data
    public static class Redis {
        private Boolean useKeyPrefix;
        private String keyPrefix;
        private Boolean cacheNullValues;
        private Duration timeToLive;
    }
}
