package com.example.web.mapper;

import com.example.service.dto.SysRoleDTO;
import com.example.web.resp.SysRoleResp;
import org.mapstruct.factory.Mappers;

public interface SysRoleTransfer {
    SysRoleTransfer INSTANCE = Mappers.getMapper(SysRoleTransfer.class);

    SysRoleResp toSysRoleResp(SysRoleDTO sysRoleDTO);

}
