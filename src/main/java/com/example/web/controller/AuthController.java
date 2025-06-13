package com.example.web.controller;

import com.example.common.Response;
import com.example.common.annotation.WebLog;
import com.example.common.util.JwtUtil;
import com.example.common.util.RsaUtil;
import com.example.event.LogoutEvent;
import com.example.security.SecurityProperties;
import com.example.security.token.JwtTokenRedisCacheProvider;
import com.example.security.utils.SecurityUtil;
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

        try {
            // 尝试从Token中解析用户信息，如果Token无效或过期，这里可能会失败
            // 注意：这里只是尝试获取用户信息用于记录日志或发布事件，
            // 即使获取失败，也应该继续执行Token的移除操作。
            Object principal = SecurityUtil.getCurrentUser(); // 这个方法内部应该有对Token的校验

            if (principal instanceof UserDTO) {
                UserDTO userDTO = (UserDTO) principal;
                log.info("用户 {} 准备登出", userDTO.getUsername());
                applicationContext.publishEvent(new LogoutEvent(this, jwtToken, userDTO.getUsername(), userDTO.getId()));
            } else if (principal != null) {
                log.warn("当前用户主体不是UserDTO类型: {}，但仍将尝试使Token失效", principal.getClass().getName());
            } else {
                // principal 为 null 的情况，可能是Token已失效，或者SecurityContext中没有认证信息
                // 这种情况通常意味着用户已经 фактически 登出，或者Token无效
                log.warn("无法获取当前用户信息，Token可能已失效或无效，将尝试移除Token: {}", jwtToken);
            }

            boolean removed = jwtTokenRedisCacheProvider.removeToken(jwtToken);
            if (removed) {
                log.info("Token已成功从缓存中移除: {}", jwtToken);
                return Response.success("登出成功");
            } else {
                // 这种情况可能是Token本身就不在缓存中（例如已经过期并被自动清理）
                // 或者Redis操作失败。对于前者，也认为是登出成功。
                log.warn("尝试从缓存中移除Token {} 失败或Token不存在", jwtToken);
                // 即使移除失败（比如Redis连接问题），从用户角度看，也应该提示登出成功，
                // 因为客户端的Token在下次请求时会被认为是无效的（如果后端有严格校验）。
                // 但服务端需要记录这个异常情况。
                return Response.success("登出成功（Token可能已失效或无法立即从缓存清除）");
            }
        } catch (Exception e) {
            log.error("登出过程中发生异常: {}", e.getMessage(), e);
            // 即使发生异常，也应该尝试让客户端认为登出成功，避免用户困惑
            // 但服务端必须记录此错误以供排查
            return Response.error(500, "登出操作异常，请稍后重试或联系管理员");
        }
    }
}
