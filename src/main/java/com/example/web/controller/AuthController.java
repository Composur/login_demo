package com.example.web.controller;

import com.example.common.Response;
import com.example.common.annotation.WebLog;
import com.example.common.util.JwtUtil;
import com.example.common.util.RsaUtil;
import com.example.event.LogoutEvent;
import com.example.security.SecurityProperties;
import com.example.security.token.JwtTokenRedisCacheProvider;
import com.example.service.CurrentUserService;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.UserTransfer;
import com.example.web.req.LoginReq;
import com.example.web.resp.LoginResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final SecurityProperties securityProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRedisCacheProvider jwtTokenRedisCacheProvider;
    private final ApplicationContext applicationContext;
    private final CurrentUserService currentUserService;


    @WebLog("用户登录接口")
    @PostMapping("/login")
    public Response login(@RequestBody LoginReq loginReq) throws Exception {
        // 1. RSA解密密码 - 使用私钥解密前端加密传输的密码
        String password = RsaUtil.decryptByPrivateKey(loginReq.getPassword(), securityProperties.getLogin().getRsaPrivateKey());

        // 2. 创建认证令牌 - 包含用户名和解密后的密码，用于后续认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), password);

        // 3. 执行认证流程 - 通过Spring Security的认证管理器进行用户认证
        Authentication authentication = null;
        try {
            // 3.1 尝试认证 - 认证失败会抛出AuthenticationException
            // 会调用UserDetailsService#loadUserByUsername(String username)
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            // 3.2 认证失败处理 - 返回错误信息和500状态码
            return Response.error(401, e.getMessage(), null);
        }

        // 4. 认证成功处理
        assert authentication != null;
        // 4.1 获取认证用户信息 - 从认证对象中提取用户主体
        UserDTO userInfo = (UserDTO) authentication.getPrincipal();
        // 4.2 转换DTO对象 - 使用MapStruct进行转换
        LoginResp response = UserTransfer.INSTANCE.toLoginResp(userInfo);

        // 5. 生成JWT Token
        String jwtToken = jwtTokenRedisCacheProvider.createToken(userInfo, loginReq);
        response.setToken(jwtToken);  // 假设LoginResp中有token字段

        // 6. 返回成功响应 - 包含用户信息、JWT Token和200状态码
        return Response.success(response);
    }

    @WebLog("用户登出接口")
    @PostMapping("/logout")
    public Response logout(HttpServletRequest request) {
        String jwtToken = JwtUtil.getTokenByRequest(request);

        if (jwtToken == null) {
            log.warn("登出请求中未找到Token");
            return Response.error(401, "登出失败，无效的会话");
        }

        // 获取当前用户信息（可选，仅用于日志和事件）
        Object principal = currentUserService.getCurrentUser();
        String username = null;
        String userId = null;
        if (principal instanceof UserDTO) {
            UserDTO userDTO = (UserDTO) principal;
            username = userDTO.getUsername();
            userId = userDTO.getId();
        }

        // 发布登出事件（可选）
        applicationContext.publishEvent(new LogoutEvent(this, jwtToken, username, userId));

        // 移除Token
        boolean removed = jwtTokenRedisCacheProvider.removeToken(jwtToken);
        if (removed) {
            log.info("Token已成功从缓存中移除: {}", jwtToken);
        } else {
            log.warn("尝试从缓存中移除Token {} 失败或Token不存在", jwtToken);
        }
        return Response.success("登出成功");
    }
}
