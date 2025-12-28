package com.example.schedule;

import com.example.dal.entity.QuartzJobEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class QuartzManager {

    private final Scheduler scheduler;

    /**
     * 添加定时任务（带 cron 表达式）
     * 构造并调度job
     */
    public void addOrUpdateJob(QuartzJobEntity quartzJob) throws SchedulerException {
        try {
            JobKey jobKey = QuartzKeyUtil.jobKey(quartzJob.getId());
            TriggerKey triggerKey = QuartzKeyUtil.triggerKey(quartzJob.getId());

            Class<? extends Job> jobClass = getJobClass(quartzJob.getJobClassName());

            // 创建 Job
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .usingJobData("parameter", quartzJob.getParameter())
                    .storeDurably()
                    .requestRecovery()
                    .build();
            // 如果 Job 已存在，addJob(detail, true) 会替换旧的 JobDetail (更新参数/类名)
            scheduler.addJob(jobDetail, true);
            log.info("创建 JobDetail: {}", jobKey);

            // 构建 Trigger
            CronScheduleBuilder scheduleBuilder =
                    CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression())
                            .withMisfireHandlingInstructionDoNothing();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(jobKey)
                    .withSchedule(scheduleBuilder)
                    .build();
            if (scheduler.checkExists(triggerKey)) {
                //    如果存在重新调度
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("重新调度 Trigger: {}", triggerKey);
            } else {
                scheduler.scheduleJob(trigger);
                log.info("创建 Trigger: {}", triggerKey);
            }

            // 确保任务不是暂停状态
            scheduler.resumeJob(jobKey);

        } catch (ClassNotFoundException e) {
            throw new SchedulerException("找不到任务类: " + quartzJob.getJobClassName(), e);
        }
    }


    /**
     * 立即执行一次任务（基于任务ID）
     */
    public void executeOnce(QuartzJobEntity quartzJob) throws SchedulerException, ClassNotFoundException {
        log.info("立即准备执行定时任务: {}", scheduler.getMetaData().getJobStoreClass());
        //log.info("JobStore = {}",
        //        scheduler.getMetaData().getJobStoreClass().getName());
        JobKey jobKey = QuartzKeyUtil.jobKey(quartzJob.getId());
        if (!scheduler.checkExists(jobKey)) {
            log.info("定时任务不存在，ID: {}", quartzJob.getId());
            // 创建 Job
            Class<? extends Job> jobClass = getJobClass(quartzJob.getJobClassName());
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .usingJobData("parameter", quartzJob.getParameter())
                    .storeDurably()
                    //.requestRecovery()
                    .build();
            // 如果 Job 已存在，addJob(detail, true) 会替换旧的 JobDetail (更新参数/类名)
            scheduler.addJob(jobDetail, true);
            log.info("创建 JobDetail: {}", jobKey);
        }

        JobDataMap dataMap = new JobDataMap();
        if (quartzJob.getParameter() != null) {
            dataMap.put("parameter", quartzJob.getParameter());
        }

        // 立即触发执行
        scheduler.triggerJob(jobKey, dataMap);
        log.info("立即触发执行定时任务: {}", jobKey);
    }

    /**
     * 删除任务（基于任务ID）
     */
    public void deleteJob(String jobId) throws SchedulerException {
        JobKey jobKey = QuartzKeyUtil.jobKey(jobId);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 暂停任务（基于任务ID）
     */
    public void pauseJob(QuartzJobEntity quartzJob) throws SchedulerException {
        String jobId = quartzJob.getId();
        JobKey jobKey = QuartzKeyUtil.jobKey(jobId);
        scheduler.pauseJob(jobKey);
    }

    private Class<? extends Job> getJobClass(String jobClassName) throws ClassNotFoundException {
        return (Class<? extends Job>) Class.forName(jobClassName);
    }
}
