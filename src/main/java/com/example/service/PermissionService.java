package com.example.service;

import com.example.dal.entity.SysApiPermissionEntity;
import com.example.dal.mapper.SysApiPermissionMapper;
import com.example.security.token.UserCacheProvider;
import com.example.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {
    private final SysApiPermissionMapper sysApiPermissionMapper;
    private final SysPermissionService sysPermissionService;
    private final UserCacheProvider userCacheProvider;

    public boolean hasPermission(String username, String url, String method) {
        // TODO: 权限校验逻辑
        // 1. 根据url和method查数据库/缓存，获取所需权限
        SysApiPermissionEntity sysApiPermissionEntity = sysApiPermissionMapper.selectByUrlAndMethod(url, method);
        if (sysApiPermissionEntity == null) {
            log.debug("接口 {} {} 未配置权限要求，默认放行", method, url);
            return true;
        }
        // 2. 查询当前用户拥有的权限
        Set<String> userPermissions = getUserPermissions(username);

        // 3. 判断用户是否有问权限
        boolean hasPermission = userPermissions.contains(sysApiPermissionEntity.getCode());
        log.debug("用户 {} 访问接口 {} {}，所需权限: {}，用户权限: {}，结果: {}",
                username, method, url, sysApiPermissionEntity.getCode(), userPermissions, hasPermission);

        return hasPermission;
    }

    /**
     * 获取用户拥有的所有权限编码
     *
     * @param username 用户名
     * @return 权限编码集合
     */
    public Set<String> getUserPermissions(String username) {
        Optional<UserDTO> userDTOOptional = userCacheProvider.getUser(username);
        if (userDTOOptional.isPresent()) {
            return userDTOOptional.get().getPermissions().stream().collect(Collectors.toSet());
        }
        // TODO
        // 2. 缓存中没有，从数据库查询
        return null;
    }
}

