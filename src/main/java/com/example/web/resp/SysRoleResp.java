package com.example.web.resp;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统角色列表项响应 DTO
 */
@Data
public class SysRoleResp {

    /**
     * 角色ID
     */
    private String id;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间 (注意：截图显示的是字符串，但通常用 LocalDateTime 更合适，序列化时会自动转为类似 "yyyy-MM-dd HH:mm:ss" 的格式)
     */
    private LocalDateTime created; // 或者 String created; 如果你严格需要截图中的格式

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用 (1: 启用, 其他: 禁用)
     */
    private Integer enabled;

    /**
     * 是否系统内置 (1: 是, 其他: 否)
     */
    private Integer isSystem;

    /**
     * 修改时间
     */
    private LocalDateTime modified; // 或者 String modified;

    /**
     * 修改者
     */
    private String modifiedBy;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;
}