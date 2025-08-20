package com.example.web.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class QuartzJobQueryPageResp {
    private String id;

    // 任务类名
    private String jobClassName;

    // 参数
    private String parameter;

    // cron 表达式
    private String cronExpression;

    // 状态 1正常 0停止
    private Integer status;

    // 描述
    private String description;

    private String createdBy;
    private Date created;
    private String modifiedBy;
    private Date modified;
}
