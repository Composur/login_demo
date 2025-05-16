package com.example.web.mapper;

import com.example.dal.entity.SysPermissionEntity;
import com.example.web.req.SysPermissionSaveReq;
import com.example.web.resp.PermissionRoutesResp;
import com.example.web.resp.SysUserMenuTreeResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysPermissionTransfer {
    SysPermissionTransfer INSTANCE = Mappers.getMapper(SysPermissionTransfer.class);

    SysPermissionEntity toSysPermissionEntity(SysPermissionSaveReq req);

    PermissionRoutesResp toPermissionRoutesResp(SysPermissionEntity sysPermissionEntity);

    SysUserMenuTreeResp toSysUserMenuTreeResp(SysPermissionEntity resp);

    // Add this default method to handle Integer to Boolean conversion for 'keepAlive'
    default Boolean map(Integer value) {
        if (value == null) {
            return false; // Or true/null depending on your business logic for null Integer.
            // false is a common default.
        }
        return value == 1;
    }
}
