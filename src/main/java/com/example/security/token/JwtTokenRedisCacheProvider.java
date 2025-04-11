package com.example.security.token;

import com.example.common.util.JwtUtil;
import com.example.config.cache.CacheUtil;
import com.example.config.cache.KeyValue;
import com.example.config.cache.RedisCacheManager;
import com.example.security.SecurityProperties;
import com.example.service.dto.UserDTO;
import com.example.web.req.LoginReq;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenRedisCacheProvider implements TokenProvider, CacheUtil {

    private final RedisCacheManager cache;

    private final SecurityProperties securityProperties;

    @Override
    public String createToken(UserDTO userDTO) {
        return createToken(userDTO, null);
    }


    @Override
    public String createToken(UserDTO userDTO, LoginReq loginReq) {
        String _jwtToken = JwtUtil.sign(userDTO.getUsername(), securityProperties.getJwt().getJwtSecret());
        //OnlineUser onlineUser = OnlineUser.convert(userDTO);
        //String json = JsonUtil.DEFAULT.toJson(onlineUser);
        String key = getCacheKey(CacheKeyValue.online_user_cache, _jwtToken);
        cache.set(key, "onlineUser", securityProperties.getJwt().getTokenValidity());
        return _jwtToken;
    }

    public boolean removeToken(String token) {
        String cacheKey = getCacheKey(CacheKeyValue.online_user_cache, token);
        return cache.del(cacheKey);
    }

    @Getter
    public enum CacheKeyValue implements KeyValue {
        online_user_cache(
                "token",
                "在线用户"
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
