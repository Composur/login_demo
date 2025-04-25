package com.example.service.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统角色数据传输对象 (Service层使用)
 */
@Data
public class SysRoleDTO {

    /**
     * 角色ID
     */
    private String id;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 修改者
     */
    private String modifiedBy;

    /**
     * 修改时间
     */
    private LocalDateTime modified;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

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
}
