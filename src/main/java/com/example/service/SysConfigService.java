package com.example.service;

import com.example.service.dto.SysConfigDTO;
import com.example.web.controller.SysConfigPageReq;
import com.example.web.resp.PageResult;

public interface SysConfigService {
    PageResult<SysConfigDTO> queryConfigByPage(SysConfigPageReq req);
}
