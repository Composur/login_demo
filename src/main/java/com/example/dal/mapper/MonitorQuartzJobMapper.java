package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.QuartzJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务Mapper接口
 */
@Mapper
public interface MonitorQuartzJobMapper extends BaseMapper<QuartzJobEntity> {
} 