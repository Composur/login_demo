package com.example.security.token;

import com.example.config.cache.CacheUtil;
import com.example.config.cache.KeyValue;
import com.example.config.cache.RedisCacheManager;
import com.example.security.SecurityProperties;
import com.example.service.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class RedisUserCacheProvider implements UserCacheProvider, CacheUtil {

    private final RedisCacheManager redisCacheManager;
    private final SecurityProperties securityProperties;

    @Override
    public boolean cacheUser(String key, UserDTO userInfo) {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(userInfo);
        } catch (JsonProcessingException e) {
            log.error("用户信息序列化失败", e);
            return false;
        }
        String cacheKey = getCacheKey(CacheKeyValue.login_user_cache, key);
        log.info("【缓存登录用户】 获取登录用户信息: key {} value {}", cacheKey, json);
        return redisCacheManager.set(cacheKey, json, securityProperties.getJwt().getTokenValidity());
    }

    /**
     * @param key key/username
     * @return
     */
    @Override
    public Optional<UserDTO> getUser(String key) {
        Optional<String> json = redisCacheManager.getStr(getCacheKey(CacheKeyValue.login_user_cache, key));
        return json.map(s -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(s, UserDTO.class);
            } catch (JsonProcessingException e) {
                log.error("用户信息反序列化失败", e);
                return null;
            }
        });
    }

    @Override
    public boolean clearUser(String key) {
        String cacheKey = getCacheKey(CacheKeyValue.login_user_cache, key);
        log.info("【缓存登录用户】清理登录用户信息: key {}", cacheKey);
        return redisCacheManager.del(cacheKey);
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
    
    /**
     * 更新用户缓存信息
     * 
     * @param userDTO 用户信息
     * @return 是否更新成功
     */
    public boolean updateUserCache(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUsername() == null) {
            return false;
        }
        return cacheUser(userDTO.getUsername(), userDTO);
    }
    
    /**
     * 批量清除用户缓存
     * 
     * @param usernames 用户名列表
     * @return 是否清除成功
     */
    public boolean clearUserBatch(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return true;
        }
        boolean result = true;
        for (String username : usernames) {
            result = result && clearUser(username);
        }
        return result;
    }
}
