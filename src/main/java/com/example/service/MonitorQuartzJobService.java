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
     * 更新定时任务
     *
     * @param req 定时任务保存请求（需包含id）
     * @return 更新结果
     */
    String update(QuartzJobSaveReq req);

    /**
     * 启动定时任务
     *
     * @param id 定时任务ID
     * @return 结果
     */
    String resume(String id);

    /**
     * 立即执行定时任务
     *
     * @param id 定时任务ID
     * @return 执行结果
     */
    String execute(String id);

    /**
     * 分页查询定时任务
     *
     * @param req 查询请求
     * @return 分页结果
     */
    PageResult<QuartzJobDTO> queryPage(QuartzJobQueryPageReq req);

    PageResult<QuartzJobDTO> queryPage2(QuartzJobQueryPageReq req);

    /**
     * 根据ID删除定时任务
     *
     * @param id 定时任务ID
     */
    void delete(String id);
}