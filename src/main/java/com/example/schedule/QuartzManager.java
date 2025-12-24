package com.example.schedule;

import com.example.dal.entity.QuartzJobEntity;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QuartzManager {
    @Autowired
    private Scheduler scheduler;

    /**
     * 添加定时任务（带 cron 表达式）
     * 构造并调度job
     */
    public void addJob(QuartzJobEntity quartzJob) throws SchedulerException {
        try {
            JobKey jobKey = QuartzKeyUtil.jobKey(quartzJob.getId());
            String jobClassName = quartzJob.getJobClassName();
            String cron = quartzJob.getCronExpression();
            String parameter = quartzJob.getParameter();

            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                return;
            }

            Class<? extends Job> jobClass = getJobClass(jobClassName);

            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .usingJobData("parameter", parameter)
                    .build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobClassName)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            throw new SchedulerException("找不到任务类: " + quartzJob.getJobClassName(), e);
        }
    }


    /**
     * 立即执行一次任务（基于任务ID）
     */
    public void executeOnce(String jobId, String jobClassName, String parameter, String description) throws SchedulerException {
        JobKey jobKey = QuartzKeyUtil.jobKey(jobId);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException("任务未启动，无法立即执行：" + jobId);
        }

        JobDataMap dataMap = new JobDataMap();
        if (parameter != null) {
            dataMap.put("parameter", parameter);
        }

        // 立即触发执行
        scheduler.triggerJob(jobKey, dataMap);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Job> getJobClass(String jobClassName) throws ClassNotFoundException {
        return (Class<? extends Job>) Class.forName(jobClassName);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
