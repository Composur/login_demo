package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.QuartzJobEntity;
import com.example.dal.mapper.MonitorQuartzJobMapper;
import com.example.schedule.QuartzManager;
import com.example.service.MonitorQuartzJobService;
import com.example.service.dto.QuartzJobDTO;
import com.example.web.mapper.QuartzJobTransfer;
import com.example.web.req.QuartzJobQueryPageReq;
import com.example.web.req.QuartzJobSaveReq;
import com.example.web.resp.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private QuartzManager quartzManager;


    @Override
    public String save(QuartzJobSaveReq req) {
        log.info("保存定时任务: {}", req);

        // 创建实体对象
        QuartzJobEntity entity = new QuartzJobEntity();
        // TODO 新增不支持操作定时任务
        //entity.setStatus(req.getStatus());
        entity.setStatus(0);
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
    public String update(QuartzJobSaveReq req) {
        log.info("更新定时任务: {}", req);

        // 验证ID是否存在
        if (req.getId() == null || req.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("更新定时任务时ID不能为空");
        }

        // 查询记录是否存在
        QuartzJobEntity existingEntity = monitorQuartzJobMapper.selectById(req.getId());
        if (existingEntity == null) {
            throw new IllegalArgumentException("定时任务不存在，ID: " + req.getId());
        }

        // 更新实体对象
        QuartzJobEntity entity = new QuartzJobEntity();
        entity.setId(req.getId());
        entity.setStatus(req.getStatus());
        entity.setJobClassName(req.getJobClassName());
        entity.setCronExpression(req.getCronExpression());
        entity.setParameter(req.getParameter());
        entity.setDescription(req.getDescription());

        // 更新到数据库
        monitorQuartzJobMapper.updateById(entity);

        // 同步状态到 Quartz
        // 启动任务
        if (entity.getStatus() == 1) {
            try {
                quartzManager.addOrUpdateJob(entity);
            } catch (SchedulerException e) {
                log.error("定时任务更新失败，ID: {}", entity.getId(), e);
                throw new RuntimeException("定时任务更新失败", e);
            }
        }
        // 暂停任务
        if (entity.getStatus() == 0) {
            try {
                quartzManager.pauseJob(entity);
            } catch (SchedulerException e) {
                log.error("定时任务更新失败，ID: {}", entity.getId(), e);
                throw new RuntimeException("定时任务更新失败", e);
            }
        }
        log.info("定时任务更新成功，ID: {}", entity.getId());
        return entity.getId();
    }

    public String start(String id) {
        QuartzJobEntity quartzJob = this.getJobOrThrow(id);
        if (quartzJob.getStatus() != null && quartzJob.getStatus() == 1) {
            return "任务运行中，请勿重复操作";
        }
        try {
            quartzManager.addOrUpdateJob(quartzJob);
            //quartzManager.getScheduler().start();
            this.updateStatus(id, 1);
        } catch (SchedulerException e) {
            log.error("定时任务启动失败，ID: {}", id, e);
            throw new RuntimeException("定时任务启动失败", e);
        }
        return id;
    }

    public String pause(String id) {
        QuartzJobEntity quartzJob = this.getJobOrThrow(id);
        if (quartzJob.getStatus() != null && quartzJob.getStatus() == 0) {
            return "任务已暂停，请勿重复操作";
        }
        try {
            quartzManager.pauseJob(quartzJob);
            this.updateStatus(id, 0);
            log.info("定时任务暂停成功，ID: {}", id);
            return id;
        } catch (SchedulerException e) {
            log.error("定时任务暂停失败，ID: {}", id, e);
            throw new RuntimeException();
        }
    }

    @Override
    public String resume(String id) {
        this.start(id);
        return id;
    }

    @Override
    public String execute(String id) {
        log.info("立即执行定时任务，ID: {}", id);

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("执行定时任务时ID不能为空");
        }

        QuartzJobEntity existingEntity = getJobOrThrow(id);
        if (existingEntity == null) {
            throw new IllegalArgumentException("定时任务不存在，ID: " + id);
        }

        try {
            String jobClassName = existingEntity.getJobClassName();
            String parameter = existingEntity.getParameter();
            String description = existingEntity.getDescription();

            quartzManager.executeOnce(id, jobClassName, parameter, description);

            log.info("定时任务立即执行成功，ID: {}, 类名: {}", id, jobClassName);
        } catch (Exception e) {
            log.error("执行定时任务失败，ID: {}", id, e);
            throw new RuntimeException("执行定时任务失败: " + e.getMessage(), e);
        }

        return id;
    }

    @Override
    public QuartzJobEntity getJobOrThrow(String id) {
        return monitorQuartzJobMapper.selectById(id);
    }

    @Override
    public void updateStatus(String id, Integer status) {
        QuartzJobEntity job = this.getJobOrThrow(id);
        job.setStatus(status);
        monitorQuartzJobMapper.updateById(job);
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

    @Override
    public void delete(String id) {
        log.info("删除定时任务，ID: {}", id);

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("删除定时任务时ID不能为空");
        }

        QuartzJobEntity existingEntity = getJobOrThrow(id);
        if (existingEntity == null) {
            throw new IllegalArgumentException("定时任务不存在，ID: " + id);
        }
        // DB
        monitorQuartzJobMapper.deleteById(id);
        // Quartz
        try {
            quartzManager.deleteJob(id);
        } catch (SchedulerException e) {
            log.error("定时任务删除失败，ID: {}", id, e);
            throw new RuntimeException("定时任务删除失败: " + e.getMessage(), e);
        }
        log.info("定时任务删除成功，ID: {}", id);
    }
} 