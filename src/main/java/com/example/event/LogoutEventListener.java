package com.example.event;

import com.example.security.token.JwtTokenRedisCacheProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogoutEventListener {

    private final JwtTokenRedisCacheProvider jwtTokenRedisCacheProvider;

    @EventListener
    public void handleLogoutEvent(LogoutEvent event) {
        String username = event.getUsername();
        String token = event.getToken();
        System.out.println("用户注销了");
        //jwtTokenRedisCacheProvider.invalidateToken(username, token);
    }
}