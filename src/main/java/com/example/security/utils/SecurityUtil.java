package com.example.security.utils;

import com.example.service.dto.UserDTO;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @date 2023/2/4
 */
@UtilityClass
public class SecurityUtil {
    /**
     * 获取当前用户
     *
     * @return .
     */
    public UserDTO getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        // 通常 UserDetails 对象应该存储在 Authentication 的 principal 中，而不是 credentials 中
        // 如果您的认证流程确实将 UserDTO 存储在 credentials 中，那么这样是正确的
        // 否则应该使用 getPrincipal()
        return (UserDTO) context.getAuthentication()
                .getCredentials();
    }

    /**
     * 获取当前用户名
     *
     * @return .
     */
    public String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前权限, 权限是: 权限+角色
     *
     * @return .
     */
    //public Collection<Authority> getCurrentPermission() {
    //    return getCurrentUser().getAuthorities();
    //}
}
