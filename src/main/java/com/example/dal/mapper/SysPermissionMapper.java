package com.example.dal.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {
    List<SysPermissionEntity> listPermission(@Param("ids") List<String> ids, @Param("is_enabled") Boolean is_enabled);
}
