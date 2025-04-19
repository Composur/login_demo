package com.example.service.impl;

import com.example.service.SysOrgService;
import com.example.service.dto.OrgListItemDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SysOrgServiceImpl implements SysOrgService {
    @Override
    public List<OrgListItemDTO> getOrgList() {
        return Collections.emptyList();
    }
}
