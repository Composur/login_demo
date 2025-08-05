package com.example.security.token;

import com.example.common.util.JwtUtil;
import com.example.config.cache.CacheUtil;
import com.example.config.cache.KeyValue;
import com.example.config.cache.RedisCacheManager;
import com.example.security.SecurityProperties;
import com.example.service.dto.OnlineUserDTO;
import com.example.service.dto.UserDTO;
import com.example.web.req.LoginReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtTokenRedisCacheProvider implements TokenProvider, CacheUtil {

    private final RedisCacheManager cache;

    private final SecurityProperties securityProperties;

    private final ObjectMapper objectMapper;


    @Override
    public boolean existsToken(String token) {
        String cacheKey = getCacheKey(CacheKeyValue.online_user_cache, token);
        return cache.hasKey(cacheKey);
    }

    @Override
    public String createToken(UserDTO userDTO) {
        return createToken(userDTO, null);
    }


    @Override
    public String createToken(UserDTO userDTO, LoginReq loginReq) {
        String token = JwtUtil.sign(userDTO.getUsername(), securityProperties.getJwt().getJwtSecret());
        OnlineUserDTO onlineUser = OnlineUserDTO.convert(userDTO);
        //String json = JsonUtil.DEFAULT.toJson(onlineUser);
        String json = objectMapper.valueToTree(onlineUser).toString();
        String cacheKey = getCacheKey(CacheKeyValue.online_user_cache, token);
        cache.set(cacheKey, json, securityProperties.getJwt().getTokenValidity());
        
        // 新增：维护 userId -> token 的集合
        String userTokensKey = getCacheKey(CacheKeyValue.user_tokens_cache, userDTO.getId());
        cache.sadd(userTokensKey, token);
        // 设置 user_tokens 的过期时间，和 token 一致
        cache.expire(userTokensKey, securityProperties.getJwt().getTokenValidity());

        return token;
    }

    public boolean removeToken(String token) {
        String cacheKey = getCacheKey(CacheKeyValue.online_user_cache, token);
        return cache.del(cacheKey);
    }

    /**
     * 获取用户token集合的缓存key
     * @param userId 用户ID
     * @return 缓存key
     */
    public String getUserTokensKey(String userId) {
        return getCacheKey(CacheKeyValue.user_tokens_cache, userId);
    }



    @Getter
    public enum CacheKeyValue implements KeyValue {
        online_user_cache(
                "token",
                "在线用户"
        ),
        user_tokens_cache(
                "user_tokens",
                "用户token集合"
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
