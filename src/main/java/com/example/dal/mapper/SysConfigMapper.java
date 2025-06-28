package com.example.dal.mapper;

import com.example.dal.entity.SysConfigEntity;
import com.example.web.controller.SysConfigPageReq;

import java.util.List;

public interface SysConfigMapper {
    List<SysConfigEntity> queryConfigByPage(SysConfigPageReq req);
}
