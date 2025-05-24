package com.example.web.mapper;

import com.example.dal.entity.SysPermissionEntity;
import com.example.web.req.SysPermissionSaveReq;
import com.example.web.resp.PermissionRoutesResp;
import com.example.web.resp.SysUserMenuTreeResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysPermissionTransfer {
    SysPermissionTransfer INSTANCE = Mappers.getMapper(SysPermissionTransfer.class);

    @Mapping(source = "enabled", target = "enabled", qualifiedByName = "booleanToInteger")
    @Mapping(source = "showLink", target = "showLink", qualifiedByName = "booleanToInteger")
    @Mapping(source = "keepAlive", target = "keepAlive", qualifiedByName = "booleanToInteger")
    @Mapping(source = "showParent", target = "showParent", qualifiedByName = "booleanToInteger")
    @Mapping(source = "perms", target = "perms")
    SysPermissionEntity toSysPermissionEntity(SysPermissionSaveReq req);

    PermissionRoutesResp toPermissionRoutesResp(SysPermissionEntity sysPermissionEntity);

    @Mapping(source = "enabled", target = "enabled", qualifiedByName = "integerToBoolean")
    @Mapping(source = "showLink", target = "showLink", qualifiedByName = "integerToBoolean")
    @Mapping(source = "keepAlive", target = "keepAlive", qualifiedByName = "integerToBoolean")
    @Mapping(source = "showParent", target = "showParent", qualifiedByName = "integerToBoolean")
    SysUserMenuTreeResp toSysUserMenuTreeResp(SysPermissionEntity resp);

    // 自定义转换方法：布尔值转整数
    @Named("booleanToInteger")
    default Integer booleanToInteger(Boolean value) {
        return value == null ? 0 : (value ? 1 : 0);
    }

    // 自定义转换方法：整数转布尔值
    @Named("integerToBoolean")
    default Boolean integerToBoolean(Integer value) {
        return value == null ? false : (value == 1);
    }
}
