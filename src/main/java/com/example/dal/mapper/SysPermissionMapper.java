package com.example.dal.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysPermissionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {
    /**
     * 查询权限根据ID
     *
     * @param ids        权限ID集合
     * @param is_enabled 是否查询启用
     * @return 权限集合
     */
    List<SysPermissionEntity> listPermission(@Param("ids") List<String> ids, @Param("is_enabled") Boolean is_enabled);

    /**
     * 根据角色ID查询权限
     *
     * @param roleIds    角色ID集合
     * @param is_enabled 是否查询启用
     * @return 权限集合
     */
    List<SysPermissionEntity> listPermissionIdsByRoleIds(@Param("roleIds") List<String> roleIds, @Param("is_enabled") Boolean is_enabled);

    /**
     * 根据ID查询权限
     *
     * @param ids
     * @param is_enabled
     * @return
     */
    List<SysPermissionEntity> list(List<String> ids, Boolean is_enabled);

    /**
     * 查询菜单根据ID
     *
     * @param ids       菜单ID集合
     * @param isEnabled 是否查询启用
     * @return 菜单集合
     */
    @Nullable
    List<SysPermissionEntity> listMenuByIds(@Nullable @Param("ids") List<String> ids,
                                            @Nullable @Param("is_enabled") Boolean isEnabled);

    /**
     * 根据用户ID查询菜单
     *
     * @param userId     用户ID
     * @param is_enabled 是否查询启用
     * @return 菜单集合
     */
    List<SysPermissionEntity> listMenuByUserId(@Param("userId") String userId, @Param("is_enabled") Boolean is_enabled);
}
