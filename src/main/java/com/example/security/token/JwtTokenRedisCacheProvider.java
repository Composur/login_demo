package com.example.security.token;

import com.example.common.util.JwtUtil;
import com.example.security.SecurityProperties;
import com.example.service.dto.UserDTO;
import com.example.web.req.LoginReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenRedisCacheProvider implements TokenProvider {

    @Autowired
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
        //String key = getCacheKey(CacheKeyValue.online_user_cache, _jwtToken);
        //cache.set(key, json, properties.getTokenValidity());
        return _jwtToken;
    }

}
