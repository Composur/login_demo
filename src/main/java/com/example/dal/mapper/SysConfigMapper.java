package com.example.dal.mapper;

import com.example.dal.entity.SysConfigEntity;
import com.example.web.controller.SysConfigPageReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysConfigMapper {
    List<SysConfigEntity> queryConfigByPage(SysConfigPageReq req);
}
