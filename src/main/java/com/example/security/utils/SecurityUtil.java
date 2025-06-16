package com.example.security.utils;

import com.example.common.BusinessException;
import com.example.common.util.SpringContextHolder;
import com.example.security.token.UserCacheProvider;
import com.example.service.dto.UserDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @date 2023/2/4
 */
@UtilityClass
@Slf4j
public class SecurityUtil {

    /**
     * 获取当前用户
     *
     * @return 当前登录用户信息，如果未登录则返回null
     */
    // public static UserDTO getCurrentUser() {
    //     try {
    //         SecurityContext context = SecurityContextHolder.getContext();
    //         // 从principal中获取用户名
    //         String username = (String) context.getAuthentication().getPrincipal();
    //         // 从Redis缓存中获取完整的用户信息
    //         return SpringContextHolder.getBean(UserCacheProvider.class)
    //                 .getUser(username)
    //                 .orElseThrow(() -> new BusinessException("用户信息不存在"));
    //     } catch (Exception e) {
    //         log.warn("获取当前用户信息失败", e);
    //         throw new BusinessException("获取当前用户信息失败");
    //     }
    // }

    /**
     * 获取当前用户名
     *
     * @return 当前用户名
     */
    public static String getCurrentUsername() {
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
