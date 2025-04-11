package com.example.security.handler;

import com.example.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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
        response.setStatus(200);  // 修改HTTP状态码为200

        // 添加调试日志
        System.out.println("异常类型: " + (authException != null ? authException.getClass().getName() : "null"));
        System.out.println("异常消息: " + (authException != null ? authException.getMessage() : "null"));
        System.out.println("内部原因: " + (authException != null && authException.getCause() != null ?
                authException.getCause().getClass().getName() : "null"));
        System.out.println("JWT_EXPIRED属性: " + request.getAttribute("JWT_EXPIRED"));

        String message;

        // 根据不同情况返回更友好的错误消息
        if (request.getAttribute("JWT_EXPIRED") != null) {
            message = "登录已过期，请重新登录";
        } else if (authException instanceof InsufficientAuthenticationException) {
            // 处理认证不足的情况
            if (authException.getCause() instanceof AuthenticationCredentialsNotFoundException) {
                // 如果内部原因是凭证无效，使用内部异常的消息
                message = authException.getCause().getMessage();
            } else if ("Full authentication is required to access this resource".equals(authException.getMessage())) {
                // 处理标准的Spring Security未认证消息
                message = "认证失败，请先登录后再访问";
            } else {
                message = "认证失败: " + authException.getMessage();
            }
        } else if (authException instanceof AuthenticationCredentialsNotFoundException) {
            // 处理凭证无效的情况
            message = authException.getMessage();
        } else {
            message = "认证失败: " + (authException != null ? authException.getMessage() : "未知错误");
        }

        response.getWriter().write(objectMapper.writeValueAsString(
                Response.error(HttpStatus.UNAUTHORIZED.value(), message, null)  // 保持自定义错误码为401
        ));
    }
}