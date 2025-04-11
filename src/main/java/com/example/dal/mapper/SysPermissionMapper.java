package com.example.dal.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {
    List<SysPermissionEntity> listPermission(@Param("ids") List<String> ids, @Param("is_enabled") Boolean is_enabled);

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
}
