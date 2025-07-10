package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysConfigEntity;
import com.example.web.controller.SysConfigPageReq;

public interface SysConfigMapper extends BaseMapper<SysConfigEntity> {
    IPage<SysConfigEntity> selectConfigPage(Page<?> page, SysConfigPageReq req);
}
