package com.example.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJobTest implements Job {
    /**
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 得到执行参数
        Object jobDataMap = context.getMergedJobDataMap().get("jobDataMap");
        System.out.println("执行任务类名：" + this.getClass().getName());
        // 执行参数
        System.out.println("jobDataMap:" + jobDataMap);
        // 执行时间
        System.out.println("执行时间：" + context.getFireTime());
    }
}
