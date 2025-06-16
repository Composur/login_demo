package com.example.service.impl;

import com.example.common.BusinessException;
import com.example.security.token.UserCacheProvider;
import com.example.security.utils.SecurityUtil;
import com.example.service.CurrentUserService;
import com.example.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserCacheProvider userCacheProvider;

    @Override
    public UserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        return userCacheProvider.getUser(username)
                .orElseThrow(() -> new BusinessException("用户未登录"));
    }

    @Override
    public Optional<UserDTO> getCurrentUserOptional() {
        try {
            String username = SecurityUtil.getCurrentUsername();
            return userCacheProvider.getUser(username);
        } catch (Exception e) {
            log.warn("获取当前用户信息失败", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getCurrentUserId() {
        return getCurrentUserOptional().map(UserDTO::getId);
    }

    @Override
    public Optional<String> getCurrentUsername() {
        return Optional.ofNullable(SecurityUtil.getCurrentUsername());
    }

    @Override
    public boolean isCurrentUserManager() {
        return getCurrentUserOptional().map(UserDTO::isManger).orElse(false);
    }

    @Override
    public boolean hasRole(String roleCode) {
        if (roleCode == null) {
            return false;
        }
        return getCurrentUserOptional()
                .map(user -> user.getRoles().contains(roleCode))
                .orElse(false);
    }

    @Override
    public boolean hasPermission(String permissionCode) {
        if (permissionCode == null) {
            return false;
        }
        return getCurrentUserOptional()
                .map(user -> user.getPermissions().contains(permissionCode))
                .orElse(false);
    }
}