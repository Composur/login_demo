package com.example.web.controller;

import com.example.common.Response;
import com.example.common.annotation.WebLog;
import com.example.common.util.RsaUtil;
import com.example.security.SecurityProperties;
import com.example.service.dto.UserDTO;
import com.example.web.req.LoginReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SecurityProperties securityProperties;
    private final AuthenticationManager authenticationManager;

    @WebLog("用户登录接口")
    @PostMapping("/login")
    public Response login(@RequestBody LoginReq loginReq) throws Exception {
        // 1. RSA解密密码
        String password = RsaUtil.decryptByPrivateKey(loginReq.getPassword(), securityProperties.getLogin().getRsaPrivateKey());
        // 2. 认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getUsername(), password);
        // 3. 授权
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            return Response.error(500, e.getMessage(), null);
        }
        assert authentication != null;
        UserDTO userInfo = (UserDTO) authentication.getPrincipal();
        // 处理登录逻辑
        return Response.success(userInfo);
    }
}
