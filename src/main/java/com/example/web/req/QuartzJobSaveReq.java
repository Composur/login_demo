package com.example.web.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QuartzJobSaveReq {

    /**
     * ID（新增时为空，更新时必填）
     */
    private String id;

    /**
     * 状态 1正常 0停止
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 任务类名
     */
    @NotBlank(message = "任务类名不能为空")
    private String jobClassName;

    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

    /**
     * 参数
     */
    private String parameter;

    /**
     * 描述
     */
    private String description;
} 