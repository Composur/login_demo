package com.example.service.impl;

import com.example.dal.entity.QuartzJobEntity;
import com.example.dal.mapper.MonitorQuartzJobMapper;
import com.example.service.MonitorQuartzJobService;
import com.example.web.req.QuartzJobSaveReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 定时任务服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorQuartzJobServiceImpl implements MonitorQuartzJobService {

    private final MonitorQuartzJobMapper monitorQuartzJobMapper;

    @Override
    public String save(QuartzJobSaveReq req) {
        log.info("保存定时任务: {}", req);

        // 创建实体对象
        QuartzJobEntity entity = new QuartzJobEntity();
        entity.setStatus(req.getStatus());
        entity.setJobClassName(req.getJobClassName());
        entity.setCronExpression(req.getCronExpression());
        entity.setParameter(req.getParameter());
        entity.setDescription(req.getDescription());

        // 保存到数据库
        monitorQuartzJobMapper.insert(entity);

        log.info("定时任务保存成功，ID: {}", entity.getId());
        return entity.getId();
    }
} 