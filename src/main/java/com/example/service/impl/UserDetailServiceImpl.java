package com.example.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.dal.entity.SysUserEntity;
import com.example.security.token.UserCacheProvider;
import com.example.service.SysPermissionService;
import com.example.service.SysUserService;
import com.example.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户详情服务实现类
 * 负责加载用户信息、角色和权限，并构建UserDetails对象
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    private final SysUserService sysUserService;
    private final SysPermissionService sysPermissionService;
    private final UserCacheProvider userCacheProvider;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 1. 加载用户基本信息
        SysUserEntity user = loadUserEntity(username);
        
        // 2. 加载用户角色和权限
        UserRolePermission rolePermission = loadUserRoleAndPermission(user);
        
        // 3. 构建并缓存UserDTO
        UserDTO userDTO = buildUserDTO(user, rolePermission);
        userCacheProvider.cacheUser(username, userDTO);
        
        log.debug("用户[{}]加载成功，角色数量：{}，权限数量：{}", 
                username, 
                rolePermission.getRoleCodes().size(), 
                rolePermission.getPermissionCodes().size());
        
        return userDTO;
    }
    
    /**
     * 加载用户实体
     */
    private SysUserEntity loadUserEntity(String username) {
        Optional<SysUserEntity> userOptional = sysUserService.loadUserByUsername(username);
        return userOptional.orElseThrow(() -> 
                new UsernameNotFoundException("根据用户名未找到用户信息：" + username));
    }
    
    /**
     * 加载用户角色和权限
     */
    private UserRolePermission loadUserRoleAndPermission(SysUserEntity user) {
        Set<String> roleCodes;
        Set<String> permissionCodes = new HashSet<>();
        
        if (user.isManager()) {
            // 管理员拥有所有角色和权限
            roleCodes = sysUserService.allRoleCode();
            permissionCodes = sysPermissionService.allPermissionCodes();
        } else {
            // 普通用户只拥有分配的角色和权限
            roleCodes = sysUserService.getRoleCodeByUsername(user.getUsername());
            
            // 根据角色获取权限
            Set<String> roleIds = sysUserService.queryRoleIdsByUserId(user.getId());
            if (CollectionUtil.isNotEmpty(roleIds)) {
                // 根据角色ID集合获取权限ID集合
                Set<String> permissionIds = sysPermissionService.listPermissionIdsByRoleIds(List.copyOf(roleIds));
                // 根据权限ID集合获取权限编码集合
                permissionCodes = sysPermissionService.listPermissionByRoleIds(List.copyOf(permissionIds));
            }
        }
        
        return new UserRolePermission(roleCodes, permissionCodes);
    }
    
    /**
     * 构建UserDTO对象
     */
    private UserDTO buildUserDTO(SysUserEntity user, UserRolePermission rolePermission) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNickname(user.getNickname());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(new ArrayList<>(rolePermission.getRoleCodes()));
        userDTO.setPermissions(new ArrayList<>(rolePermission.getPermissionCodes()));
        userDTO.setManger(user.isManager());
        userDTO.setOrgId(user.getOrgId()); // 假设SysUserEntity有orgId字段
        
        // 设置账户状态，这里假设所有账户都是有效的
        userDTO.setAccountNonExpired(true);
        userDTO.setAccountNonLocked(true);
        userDTO.setCredentialsNonExpired(true);
        userDTO.setEnabled(true);
        
        return userDTO;
    }
    
    /**
     * 用户角色和权限数据类
     */
    private static class UserRolePermission {
        private final Set<String> roleCodes;
        private final Set<String> permissionCodes;
        
        public UserRolePermission(Set<String> roleCodes, Set<String> permissionCodes) {
            this.roleCodes = roleCodes;
            this.permissionCodes = permissionCodes;
        }
        
        public Set<String> getRoleCodes() {
            return roleCodes;
        }
        
        public Set<String> getPermissionCodes() {
            return permissionCodes;
        }
    }
}
