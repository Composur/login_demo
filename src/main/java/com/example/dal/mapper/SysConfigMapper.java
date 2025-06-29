package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysConfigEntity;
import com.example.web.controller.SysConfigPageReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysConfigMapper {
    IPage<SysConfigEntity> selectConfigPage(Page<?> page, SysConfigPageReq req);
}
