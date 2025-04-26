package com.example.web.mapper;

import com.example.dal.entity.SysRoleEntity;
import com.example.service.dto.SysRoleDTO;
import com.example.web.resp.SysRoleResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysRoleTransfer {
    SysRoleTransfer INSTANCE = Mappers.getMapper(SysRoleTransfer.class);

    SysRoleResp toSysRoleResp(SysRoleDTO sysRoleDTO);

    SysRoleDTO toSysRoleDTO(SysRoleEntity sysRoleEntity);
}
