package com.example.service;

import com.example.service.dto.UserDTO;

import java.util.Optional;

public interface CurrentUserService {
    /**
     * 获取当前登录用户信息
     *
     * @return 当前用户信息
     */
    UserDTO getCurrentUser();


    /**
     * 获取当前登录用户信息
     *
     * @return 当前用户信息
     */
    Optional<UserDTO> getCurrentUserOptional();

    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    Optional<String> getCurrentUserId();

    /**
     * 获取当前用户名
     *
     * @return 当前用户名
     */
    Optional<String> getCurrentUsername();

    /**
     * 判断当前用户是否为管理员
     *
     * @return 是否为管理员
     */
    boolean isCurrentUserManager();

    /**
     * 判断当前用户是否拥有指定角色
     *
     * @param roleCode 角色编码
     * @return 是否拥有该角色
     */
    boolean hasRole(String roleCode);

    /**
     * 判断当前用户是否拥有指定权限
     *
     * @param permissionCode 权限编码
     * @return 是否拥有该权限
     */
    boolean hasPermission(String permissionCode);
}