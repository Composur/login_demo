package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username .
     * @return .
     */
    SysUserEntity getByUsername(@Param("username") String username);

    /**
     * 根据用户名获取用户信息
     *
     * @return .
     */
    Set<String> allRoleCode();
}
