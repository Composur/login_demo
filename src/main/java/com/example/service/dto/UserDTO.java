package com.example.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户数据传输对象，用于封装用户信息并实现 UserDetails 接口。
 */
@Data
public class UserDTO implements UserDetails {
    private String id;
    private String username;
    private String nickname;
    private String password; // 新增密码字段
    private List<String> roles; // 新增角色列表
    private List<String> permissions; // 新增权限列表
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean isManger;
    /**
     * 所属机构
     */
    private String orgId;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 添加角色（格式：ROLE_角色名）
        if (roles != null) {
            authorities.addAll(
                roles.stream()
                    .filter(role -> role != null && !role.trim().isEmpty()) // 过滤空值
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList())
            );
        }
        
        // 添加权限（格式：权限编码，如 sys:config:query）
        if (permissions != null) {
            authorities.addAll(
                permissions.stream()
                    .filter(permission -> permission != null && !permission.trim().isEmpty()) // 过滤空值
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
            );
        }
        
        return authorities;
    }
    

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}