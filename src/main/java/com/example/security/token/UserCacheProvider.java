package com.example.security.token;

import com.example.service.dto.UserDTO;

import java.util.Optional;

public interface UserCacheProvider {
    /**
     * 缓存登录用户
     *
     * @param key      key/username
     * @param userInfo 登录用户信息
     * @return .
     */
    boolean cacheUser(String key, UserDTO userInfo);

    /**
     * 获取登录用户
     *
     * @param key key/username
     * @return .
     */
    Optional<UserDTO> getUser(String key);

    /**
     * 清理登录用户
     *
     * @param key key/username
     * @return .
     */
    boolean clearUser(String key);


}
