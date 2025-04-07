package com.example.web.mapper;

import com.example.service.dto.UserDTO;
import com.example.web.resp.LoginResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserTransfer {
    UserTransfer INSTANCE = Mappers.getMapper(UserTransfer.class);

    LoginResp toLoginResp(UserDTO userDTO);
}