package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysOrgEntity;
import com.example.service.dto.OrgListItemDTO;

import java.util.List;

public interface SysOrgMapper extends BaseMapper<SysOrgEntity> {
    List<OrgListItemDTO> queryOrgList();
}
