package com.example.service.impl;

import com.example.dal.entity.SysUser;
import com.example.service.SysUserService;
import com.example.service.UserService;
import com.example.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<SysUser> userOptional = sysUserService.loadUserByUsername(username);
        SysUser user = userOptional.orElseThrow(() -> new UsernameNotFoundException("根据用户名未找到用户信息"));
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword()); // 注意：此处应为加密后的密码
        userDTO.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER")); // 符合 ROLE_ 前缀规范 [[4]][[6]]
        return userDTO;
    }
}
