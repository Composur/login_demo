package com.example.security.filter;

import cn.hutool.core.util.StrUtil;
import com.example.common.util.JwtUtil;
import com.example.security.SecurityProperties;
import com.example.security.handler.JwtAuthenticationEntryPoint;
import com.example.security.token.UserCacheProvider;
import com.example.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
 * JWT令牌认证过滤器
 * 负责从请求中提取JWT令牌，验证其有效性，并将用户认证信息设置到Spring Security上下文中
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailServiceImpl userDetailService;
    private final SecurityProperties securityProperties;
    private final UserCacheProvider userCacheProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. 从请求中提取JWT令牌
        String jwtToken = JwtUtil.getTokenByRequest(request);

        // 2. 如果请求中包含JWT令牌，则进行验证
        if (StrUtil.isNotBlank(jwtToken)) {
            authenticateToken(request, jwtToken);
        }
        // 3. 继续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 验证JWT令牌并设置认证信息
     *
     * @param request  请求对象
     * @param jwtToken JWT令牌
     * @throws AuthenticationCredentialsNotFoundException 如果令牌无效或过期
     */
    private void authenticateToken(HttpServletRequest request, String jwtToken) {
        // 1. 从JWT令牌中提取用户名
        String username = JwtUtil.getUsername(jwtToken);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("无效的JWT令牌");
        }

        // 2. 验证JWT令牌的签名和过期时间
        if (!JwtUtil.verify(jwtToken, username, securityProperties.getJwt().getJwtSecret())) {
            // JWT令牌无效或已过期
            userCacheProvider.clearUser(username);
            request.setAttribute("JWT_EXPIRED", true); // 标记为过期
            throw new AuthenticationCredentialsNotFoundException("登录过期");
        }

        // 3. 令牌有效，加载用户详细信息
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // 4. 如果用户详细信息不为空且安全上下文为空，则设置认证信息
        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
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
            log.debug("用户[{}]认证成功，设置安全上下文", username);
        }
    }
}