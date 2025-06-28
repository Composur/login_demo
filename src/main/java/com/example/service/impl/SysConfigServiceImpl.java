package com.example.service.impl;

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

    /**
     * @param req
     * @return
     */
    @Override
    public PageResult<SysConfigDTO> queryConfigByPage(SysConfigPageReq req) {
        List<SysConfigEntity> sysConfigEntities = sysConfigMapper.queryConfigByPage(req);
        List<SysConfigDTO> sysConfigDTOS = sysConfigEntities.stream().map(SysConfigTransfer.INSTANCE::toSysConfigDTO).collect(Collectors.toList());
        return null;
    }
}
