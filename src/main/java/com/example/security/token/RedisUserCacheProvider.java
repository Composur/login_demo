package com.example.security.token;

import com.example.config.cache.AppCache;
import com.example.config.cache.CacheUtil;
import com.example.config.cache.KeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUserCacheProvider implements UserCacheProvider, CacheUtil {

    @Autowired
    private final AppCache cache;

    @Override
    public boolean clearUser(String key) {
        String cacheKey = getCacheKey(CacheKeyValue.login_user_cache, key);
        log.info("【缓存登录用户】清理登录用户信息: key {}", cacheKey);
        return cache.del(cacheKey);
    }

    @Getter
    public enum CacheKeyValue implements KeyValue {
        login_user_cache(
                "login-user",
                "登录用户"
        ),
        ;
        private final String prefix;
        private final String name;

        CacheKeyValue(String prefix, String name) {
            this.prefix = prefix;
            this.name = name;
        }
    }
}
