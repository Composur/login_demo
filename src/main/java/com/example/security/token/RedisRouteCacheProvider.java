package com.example.security.token;

import com.example.config.cache.CacheUtil;
import com.example.config.cache.KeyValue;
import com.example.config.cache.RedisCacheManager;
import com.example.security.SecurityProperties;
import com.example.web.resp.PermissionRoutesResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisRouteCacheProvider implements RouteCacheProvider, CacheUtil {

    private final RedisCacheManager redisCacheManager;
    // 你可以根据实际情况注入配置类，比如缓存有效期
    private final SecurityProperties securityProperties;

    private final ObjectMapper objectMapper;


    private static final int CACHE_EXPIRE_SECONDS = 3600; // 1小时，可根据需要调整

    @Override
    public boolean cacheRoutes(String userId, List<PermissionRoutesResp> routes) {
        //ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(routes);
        } catch (JsonProcessingException e) {
            log.error("路由信息序列化失败", e);
            return false;
        }
        String cacheKey = getCacheKey(CacheKeyValue.route_cache, userId);
        log.info("【缓存路由】key: {} value: {}", cacheKey, json);
        return redisCacheManager.set(cacheKey, json, securityProperties.getJwt().getTokenValidity());
    }

    @Override
    public Optional<List<PermissionRoutesResp>> getRoutes(String userId) {
        Optional<String> json = redisCacheManager.getStr(getCacheKey(CacheKeyValue.route_cache, userId));
        return json.map(s -> {
            //ObjectMapper mapper = new ObjectMapper();
            try {
                return objectMapper.readValue(s, new TypeReference<List<PermissionRoutesResp>>() {
                });
            } catch (JsonProcessingException e) {
                log.error("路由信息反序列化失败", e);
                return null;
            }
        });
    }

    @Override
    public boolean clearRoutes(String userId) {
        String cacheKey = getCacheKey(CacheKeyValue.route_cache, userId);
        log.info("【缓存路由】清理路由缓存: key {}", cacheKey);
        return redisCacheManager.del(cacheKey);
    }

    @Getter
    public enum CacheKeyValue implements KeyValue {
        route_cache(
                "user-route",
                "用户路由"
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