package com.example.service.impl;

import com.example.dal.mapper.SysOrgMapper;
import com.example.service.SysOrgService;
import com.example.service.dto.OrgListItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SysOrgServiceImpl implements SysOrgService {
    private final SysOrgMapper sysOrgMapper;

    @Override
    public List<OrgListItemDTO> getOrgList() {
        return sysOrgMapper.queryOrgList();
    }
}
