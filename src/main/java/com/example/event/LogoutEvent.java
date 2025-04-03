package com.example.event;

import org.springframework.context.ApplicationEvent;

/**
 * logout event
 */
public class LogoutEvent extends ApplicationEvent {
    /**
     * 访问token
     */
    private final String token;
    /**
     * 用户名
     */
    private final String username;
    /**
     * 用户ID
     */
    private final String userid;

    public LogoutEvent(Object source, String token, String username, String userid) {
        super(source);
        this.token = token;
        this.username = username;
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }
}
