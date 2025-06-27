package com.example.service.impl;

import com.example.service.SysConfigService;
import com.example.service.dto.SysConfigDTO;
import com.example.web.controller.SysConfigPageReq;
import com.example.web.resp.PageResult;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    /**
     * @param req
     * @return
     */
    @Override
    public PageResult<SysConfigDTO> queryConfigByPage(SysConfigPageReq req) {
        return null;
    }
}
