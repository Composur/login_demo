package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.QuartzJobEntity;
import com.example.dal.mapper.MonitorQuartzJobMapper;
import com.example.service.MonitorQuartzJobService;
import com.example.service.dto.QuartzJobDTO;
import com.example.web.mapper.QuartzJobTransfer;
import com.example.web.req.QuartzJobQueryPageReq;
import com.example.web.req.QuartzJobSaveReq;
import com.example.web.resp.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageResult<QuartzJobDTO> queryPage2(QuartzJobQueryPageReq req) {
        Page<QuartzJobEntity> page = new Page<>(req.getCurrent(), req.getSize());
        LambdaQueryWrapper<QuartzJobEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(req.getJobClassName())) {
            String kw = req.getJobClassName();
            wrapper.and(w -> w.like(QuartzJobEntity::getJobClassName, kw)
                    .or()
                    .like(QuartzJobEntity::getDescription, kw));
        }
        if (req.getStatus() != null) {
            wrapper.eq(QuartzJobEntity::getStatus, req.getStatus());
        }

        IPage<QuartzJobEntity> resultPage = monitorQuartzJobMapper.selectPage(page, wrapper);

        List<QuartzJobDTO> dtoList = resultPage.getRecords().stream()
                .map(QuartzJobTransfer.INSTANCE::toQuartzJobDTO)
                .collect(Collectors.toList());

        return new PageResult<>(
                dtoList,
                resultPage.getTotal(),
                resultPage.getCurrent(),
                resultPage.getSize()
        );
    }

    @Override
    public PageResult<QuartzJobDTO> queryPage(QuartzJobQueryPageReq req) {
        Page<QuartzJobEntity> page = new Page<>(req.getCurrent(), req.getSize());
        IPage<QuartzJobEntity> quartzJobEntityIPage = monitorQuartzJobMapper.selectQuartzJobPage(page, req);
        List<QuartzJobDTO> quartzJobDTOS = quartzJobEntityIPage.getRecords()
                .stream()
                .map(quartzJobEntity -> QuartzJobTransfer.INSTANCE.toQuartzJobDTO(quartzJobEntity))
                .collect(Collectors.toList());
        PageResult<QuartzJobDTO> respPage = new PageResult<>(
                quartzJobDTOS,
                quartzJobEntityIPage.getTotal(),
                quartzJobEntityIPage.getCurrent(),
                quartzJobEntityIPage.getSize()
        );
        return respPage;
    }
} 