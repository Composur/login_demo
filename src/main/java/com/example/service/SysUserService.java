package com.example.service;

import com.example.dal.entity.SysUser;
import com.example.dal.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return .
     */
    public Optional<SysUser> loadUserByUsername(String username) {
        //LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(SysUser.class)
        //        .eq(SysUser::getUsername, username);
        //return Optional.ofNullable(getOne(queryWrapper));
        SysUser sysUser = sysUserMapper.getByUsername(username);
        return Optional.ofNullable(sysUser);
    }

}
