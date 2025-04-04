package com.example.security.handler;

import com.example.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message = "认证失败: " + (authException != null ? authException.getMessage() : "未知错误");
        if (request.getAttribute("JWT_EXPIRED") != null) {
            message = "登录已过期，请重新登录";
        }

        response.getWriter().write(objectMapper.writeValueAsString(
                Response.error(HttpStatus.UNAUTHORIZED.value(), message, null)
        ));
    }
}