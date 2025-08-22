package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.QuartzJobEntity;
import com.example.web.req.QuartzJobQueryPageReq;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务Mapper接口
 */
@Mapper
public interface MonitorQuartzJobMapper extends BaseMapper<QuartzJobEntity> {
    IPage<QuartzJobEntity> selectQuartzJobPage(Page<?> page, QuartzJobQueryPageReq req);
} 