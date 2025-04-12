package com.example.service.impl;

import com.example.dal.entity.SysUserEntity;
import com.example.service.SysPermissionService;
import com.example.service.SysUserService;
import com.example.service.UserService;
import com.example.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final SysUserService sysUserService;
    private final SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<SysUserEntity> userOptional = sysUserService.loadUserByUsername(username);
        SysUserEntity user = userOptional.orElseThrow(() -> new UsernameNotFoundException("根据用户名未找到用户信息"));
        // 查询权限
        // 角色code
        Set<String> roleCodes;
        // 权限code
        Set<String> preCodes = new HashSet<>();
        // 管理员
        if (user.isManager()) {
            roleCodes = sysUserService.allRoleCode();
            preCodes = sysPermissionService.allPermissionCodes();
        } else {
            roleCodes = sysUserService.getRoleCodeByUsername(user.getUsername());
            preCodes.add("user:*");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(new ArrayList<>(roleCodes));
        userDTO.setPermissions(new ArrayList<>(preCodes));
        // 设置 isManger 字段
        userDTO.setManger(user.isManager());
        return userDTO;
    }
}
