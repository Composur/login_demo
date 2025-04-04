package com.example.security.token;

import com.example.service.dto.UserDTO;
import com.example.web.req.LoginReq;

public interface TokenProvider {
    String createToken(UserDTO userDTO);

    String createToken(UserDTO userDTO, LoginReq loginReq);

    boolean removeToken(String token);
}
