package com.example.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 提供对密码的加密与匹对
 */
public class PasswordUtil {
    /**
     * 将原始密码进行编码,使用默认编码器{@link #defaultPasswordEncoder()}
     *
     * @param rawPassword 原密码
     * @return 编码后的密码
     */
    public static String encoder(String rawPassword) {
        return encoder(rawPassword, defaultPasswordEncoder());
    }

    /**
     * 将原始密码进行编码
     *
     * @param rawPassword     原密码
     * @param passwordEncoder 编码器
     * @return 编码后的密码
     */
    public static String encoder(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 校验原始密码与编码后的密码是否为同一个密码，使用默认编码器{@link #defaultPasswordEncoder()}
     *
     * @param rawPassword     原密码
     * @param encodedPassword 编码后的密码
     * @return 是否一致
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return matches(rawPassword, encodedPassword, defaultPasswordEncoder());
    }

    /**
     * 校验原始密码与编码后的密码是否为同一个密码
     *
     * @param rawPassword     原密码
     * @param encodedPassword 编码后的密码
     * @param passwordEncoder 编码器
     * @return 是否一致
     */
    public static boolean matches(String rawPassword, String encodedPassword,
                                  PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    /**
     * 默认使用 {@link  BCryptPasswordEncoder} 密码编码器
     *
     * @return {@link  BCryptPasswordEncoder}
     */
    public static PasswordEncoder defaultPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
