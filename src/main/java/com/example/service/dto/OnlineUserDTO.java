package com.example.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 在线用户
 */
@Data
@EqualsAndHashCode
@ToString
public class OnlineUserDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 岗位
     */
    private String dept;
    /**
     * token
     */
    private String key;
    /**
     * 浏览器
     */
    private String browser;

    /**
     * IP
     */
    private String ip;

    /**
     * 地址
     */
    private String address;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;


    public static OnlineUserDTO convert(UserDTO userInfo) {
        OnlineUserDTO onlineUser = new OnlineUserDTO();
        onlineUser.setUsername(userInfo.getUsername());
        return onlineUser;
    }
}
