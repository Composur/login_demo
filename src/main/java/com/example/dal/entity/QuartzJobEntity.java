package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_quartz_job")
public class QuartzJobEntity extends BaseEntity {

    /**
     * 任务类名
     */
    @TableField("job_class_name")
    private String jobClassName;

    /**
     * 参数
     */
    private String parameter;

    /**
     * cron表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * 状态 1正常 0停止
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;
} 