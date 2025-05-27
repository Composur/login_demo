package com.example.web.mapper;

import com.example.dal.entity.SysRoleEntity;
import com.example.service.dto.RoleQueryDTO;
import com.example.service.dto.SysRoleDTO;
import com.example.web.req.SysRolePageReq;
import com.example.web.resp.SysRoleResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper
public interface SysRoleTransfer {
    SysRoleTransfer INSTANCE = Mappers.getMapper(SysRoleTransfer.class);

    SysRoleResp toSysRoleResp(SysRoleDTO sysRoleDTO);

    SysRoleDTO toSysRoleDTO(SysRoleEntity sysRoleEntity);
    
    @Mapping(target = "roleCodes", expression = "java(roleCodeToList(req.getRoleCode()))")
    RoleQueryDTO toRoleQueryDTO(SysRolePageReq req);
    
    /**
     * 将单个roleCode转换为List<String>
     */
    default List<String> roleCodeToList(String roleCode) {
        return roleCode == null ? null : Collections.singletonList(roleCode);
    }
}
