package com.example.security.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
     * @return 当前用户名，若无法获取则返回 null
     */
    public static String getCurrentUsername() {
        var context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        var authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }

        var principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return (String) principal;
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            // 其他未知类型的 Principal，避免 ClassCastException
            return null;
        }
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
