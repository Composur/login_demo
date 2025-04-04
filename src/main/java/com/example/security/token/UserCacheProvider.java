package com.example.security.token;

public interface UserCacheProvider {
    /**
     * 清理登录用户
     *
     * @param key key/username
     * @return .
     */
    boolean clearUser(String key);
}
