package com.example.config.cache;

import com.example.common.enums.DefaultKeyValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisConfigCacheProvider implements ConfigCacheProvider, CacheUtil {
    private final RedisCacheManager redisCacheManager;

    /**
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean cacheConfig(String key, String value) {
        boolean set = redisCacheManager.set(getCacheKey(DefaultKeyValue.SYS_CONFIG, key), value, DefaultKeyValue.EXPIRE_TIME_DEFAULT);
        return set;
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Optional<String> getConfig(String key) {
        return redisCacheManager.getStr(getCacheKey(DefaultKeyValue.SYS_CONFIG, key));
    }

    /**
     * @param key
     * @return
     */
    @Override
    public boolean clearConfig(String key) {
        return redisCacheManager.del(getCacheKey(DefaultKeyValue.SYS_CONFIG, key));
    }
}
