package com.example.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "security") // 指定配置前缀 [[8]]
@Configuration
@Data
public class SecurityProperties {
    private Jwt jwt;
    private Login login;

    @Data
    public static class Jwt {
        private Integer tokenValidity; // token有效时间（秒）
        private Boolean delayToken;    // 自动延长token
        private Integer tokenDetect;   // token续期检查时间（秒）
        private String jwtSecret;      // JWT密钥
    }

    @Data
    public static class Login {
        private String rsaPrivateKey;  // 登录私钥
        private String rsaPublicKey;   // 登录公钥
    }
}