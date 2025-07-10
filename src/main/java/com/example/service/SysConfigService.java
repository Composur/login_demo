package com.example.service;

import com.example.service.dto.SysConfigDTO;
import com.example.web.controller.SysConfigPageReq;
import com.example.web.resp.PageResult;

import java.util.Set;

public interface SysConfigService {
    PageResult<SysConfigDTO> queryConfigByPage(SysConfigPageReq req);

    SysConfigDTO save(SysConfigDTO sysConfigDTO);

    SysConfigDTO update(String id, SysConfigDTO sysConfigDTO);

    void deleteByIds(Set<String> ids);

    void refreshCache(String configCode);
    
    void refreshAllCache();
}
