package com.example.service;

import com.example.web.req.QuartzJobSaveReq;

/**
 * 定时任务服务接口
 */
public interface MonitorQuartzJobService {

    /**
     * 保存定时任务
     *
     * @param req 定时任务保存请求
     * @return 保存结果
     */
    String save(QuartzJobSaveReq req);
} 