package com.example.web.controller;

import com.example.web.req.BasePageReq;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SysConfigPageReq extends BasePageReq {
    /**
     * 配置类型
     */
    private String type;

    /**
     * 配置编码
     */
    private String code;
}
