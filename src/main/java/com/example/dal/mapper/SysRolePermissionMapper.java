package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysRolePermissionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermissionEntity> {

    /**
     * 根据权限ID获取所有的父级ID
     *
     * @param permissionIds .
     * @return .
     */
    List<String> getParentIdByPermissionIds(@Param("permissionIds") List<String> permissionIds);

    /**
     * 根据角色ID查询权限ID,只查询没有下级的权限
     *
     * @param roleId .
     * @return .
     */
    List<String> getPermissionIdByRoleId(@Param("roleId") String roleId);

    /**
     * 批量插入
     *
     * @param list .
     * @return .
     */
    //int insertBatch(List<SysRolePermissionEntity> rolePermissionEntities);
    int insertBatch(@Param("list") List<SysRolePermissionEntity> list);
}
