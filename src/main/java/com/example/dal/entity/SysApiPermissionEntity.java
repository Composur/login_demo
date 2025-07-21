package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统接口权限实体类
 * 对应数据库表 sys_api_permission
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_api_permission")
public class SysApiPermissionEntity extends BaseEntity {

    /**
     * 接口URL
     */
    @TableField("url")
    private String url;

    /**
     * HTTP方法
     */
    @TableField("method")
    private String method;

    /**
     * 权限编码
     */
    @TableField("code")
    private String code;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;
}