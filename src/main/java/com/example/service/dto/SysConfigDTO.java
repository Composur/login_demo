package com.example.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SysConfigDTO {
    // 主键ID
    private String id;

    // 配置项编码
    private String code;

    // 配置项类型
    private Integer type;

    // 配置项类型名称
    private String typeName;

    // 配置项值
    private String value;

    // 备注信息
    private String memo;

    private String createdBy;
    private Date created;
    private String modifiedBy;
    private Date modified;
}

