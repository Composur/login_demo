package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysOrgEntity;
import com.example.service.dto.OrgListItemDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysOrgMapper extends BaseMapper<SysOrgEntity> {
    List<OrgListItemDTO> queryOrgList();
}
