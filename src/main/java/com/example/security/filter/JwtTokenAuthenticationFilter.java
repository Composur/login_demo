package com.example.security.filter;

import cn.hutool.core.util.StrUtil;
import com.example.common.util.JwtUtil;
import com.example.security.SecurityProperties;
import com.example.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token认证过滤器
 */
@RequiredArgsConstructor
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailServiceImpl userDetailService;
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求中提取 JWT 令牌
        String _jwtToken = JwtUtil.getTokenByRequest(request);
        if (StrUtil.isNotBlank(_jwtToken)) {
            try {
                // 2. 验证 JWT 令牌并提取用户名
                String username = JwtUtil.getUsername(_jwtToken);

                if (username != null) {
                    // 3. 加载用户详细信息
                    UserDetails userDetails = userDetailService.loadUserByUsername(username);

                    // 4. 验证 JWT 令牌的签名和过期时间
                    if (userDetails != null && JwtUtil.verify(_jwtToken, username, securityProperties.getJwt().getJwtSecret())) {
                        // 5. 如果用户详细信息不为空且安全上下文为空，则设置认证信息
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails.getUsername(),
                                            userDetails,
                                            userDetails.getAuthorities()
                                    );
                            authenticationToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    } else {
                        // JWT 令牌验证失败
                        System.out.println("JWT 令牌验证失败: 签名或过期时间无效");
                    }
                }
            } catch (Exception e) {
                // 处理 JWT 验证失败的情况
                // 例如，记录日志或返回错误响应
                System.out.println("JWT 验证失败: {}" + e.getMessage());
            }
        }
        // 6. 继续过滤链
        filterChain.doFilter(request, response);
    }
}