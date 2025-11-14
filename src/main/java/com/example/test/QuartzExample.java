package com.example.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzExample {
    public static void main(String[] args) throws Exception {
        // 1️⃣ 定义要执行的任务
        JobDetail job = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "group1")
                .build();

        // 2️⃣ 定义触发器：每隔 10 秒执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        // 3️⃣ 获取调度器并启动
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    // 自定义任务
    public static class MyJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("执行任务时间：" + System.currentTimeMillis());
        }
    }
}
