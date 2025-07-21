package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysApiPermissionEntity;
import org.apache.ibatis.annotations.Param;

public interface SysApiPermissionMapper extends BaseMapper<SysApiPermissionEntity> {
    SysApiPermissionEntity selectByUrlAndMethod(@Param("url") String url, @Param("method") String method);
}
