package com.example.web.req;

import lombok.Data;

/**
 * 基础分页查询请求参数
 */
@Data
public class BasePageReq {
    /**
     * 当前页码
     */
    private Long current = 1L;

    /**
     * 每页数量
     */
    private Long size = 10L;
} 