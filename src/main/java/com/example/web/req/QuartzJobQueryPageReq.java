package com.example.web.req;

import lombok.Data;

@Data
public class QuartzJobQueryPageReq extends BasePageReq {
    /**
     * 任务类名
     */
    private String jobClassName;
    /**
     * 是否启用
     */
    private Integer status;
}
