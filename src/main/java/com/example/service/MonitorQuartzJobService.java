package com.example.service;

import com.example.service.dto.QuartzJobDTO;
import com.example.web.req.QuartzJobQueryPageReq;
import com.example.web.req.QuartzJobSaveReq;
import com.example.web.resp.PageResult;

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

    /**
     * 分页查询定时任务
     *
     * @param req 查询请求
     * @return 分页结果
     */
    PageResult<QuartzJobDTO> queryPage(QuartzJobQueryPageReq req);
} 