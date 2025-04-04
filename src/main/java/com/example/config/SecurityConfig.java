package com.example.config;

import com.example.common.util.PasswordUtil;
import com.example.security.filter.JwtTokenAuthenticationFilter;
import com.example.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final CorsFilter corsFilter;
    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;
    //private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 使用自定义认证入口点
                .and()
                .userDetailsService(userDetailsService)
                .headers()
                .frameOptions().disable() // 防止iframe 造成跨域
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 会话管理，无状态
                .and()
                .formLogin().disable() // 禁用表单登录
                .httpBasic().disable() // 禁用HTTP基本认证
                .authorizeRequests()
                .antMatchers("/auth/**", "/public/**").permitAll() // 允许登录接口无需鉴权
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // options 开放
                .anyRequest().authenticated() // 其余认证访问
                .and()
                .addFilterBefore(corsFilter, CorsFilter.class) // 添加CorsFilter
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加JWT过滤器
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordUtil.defaultPasswordEncoder();
    }
}