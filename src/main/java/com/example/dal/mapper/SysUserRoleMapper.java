package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {
    /**
     * 根据角色ID查询用户ID
     *
     * @param roleId 角色ID
     * @return 用户ID集合
     */
    List<String> getUserIdsByRoleId(@Param("roleId") String roleId);
}