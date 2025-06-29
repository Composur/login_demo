package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysConfigEntity;
import com.example.dal.mapper.SysConfigMapper;
import com.example.service.SysConfigService;
import com.example.service.dto.SysConfigDTO;
import com.example.web.controller.SysConfigPageReq;
import com.example.web.mapper.SysConfigTransfer;
import com.example.web.resp.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;


    @Override
    public PageResult<SysConfigDTO> queryConfigByPage(SysConfigPageReq req) {
        Page<SysConfigEntity> page = new Page<>(req.getCurrent(), req.getSize());
        IPage<SysConfigEntity> sysConfigEntities = sysConfigMapper.selectConfigPage(page, req);
        List<SysConfigDTO> sysConfigDTOS = sysConfigEntities.getRecords().stream().map(SysConfigTransfer.INSTANCE::toSysConfigDTO).collect(Collectors.toList());
        PageResult<SysConfigDTO> respPage = new PageResult<>(sysConfigDTOS, sysConfigEntities.getTotal(), sysConfigEntities.getCurrent(), sysConfigEntities.getSize());
        return respPage;
    }
}
