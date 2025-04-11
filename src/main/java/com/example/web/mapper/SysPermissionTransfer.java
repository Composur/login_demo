package com.example.web.mapper;

import com.example.dal.entity.SysPermissionEntity;
import com.example.web.resp.PermissionRoutesResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysPermissionTransfer {
    SysPermissionTransfer INSTANCE = Mappers.getMapper(SysPermissionTransfer.class);

    PermissionRoutesResp toPermissionRoutesResp(SysPermissionEntity sysPermissionEntity);
}
