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
