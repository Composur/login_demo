package com.example.event;

import com.example.security.token.UserCacheProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutEventListener {
    private final UserCacheProvider userCacheProvider;

    @EventListener
    public void handleLogoutEvent(LogoutEvent event) {
        log.info("【用户注销事件】用户注销清理缓存信息>>>>>>>>>>>开始");
        String username = event.getUsername();
        userCacheProvider.clearUser(username);
        log.info("【用户注销事件】成功清理缓存登录用户信息");
        //routeCache.removeCache(event.getUserid());
        log.info("【用户注销事件】成功清理缓存用户路由信息");
    }
}