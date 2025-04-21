package com.example.web.mapper;

import com.example.dal.entity.SysUserEntity;
import com.example.service.dto.UserDTO;
import com.example.web.resp.LoginResp;
import com.example.web.resp.SysUserResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserTransfer {
    UserTransfer INSTANCE = Mappers.getMapper(UserTransfer.class);

    LoginResp toLoginResp(UserDTO userDTO);

    // --- Entity <-> DTO ---
    UserDTO toUserDto(SysUserEntity entity);

    // 添加 Entity 列表到 DTO 列表的映射方法签名
    List<UserDTO> toUserDtoList(List<SysUserEntity> entities);

    // --- DTO -> Resp ---
    SysUserResp toSysUserResp(UserDTO userDTO);

    // 添加 DTO 列表到 Resp 列表的映射方法签名
    List<SysUserResp> toSysUserRespList(List<UserDTO> dtos);
}