package com.example.web.req;

import lombok.Data;

/**
 * 角色分页查询请求参数
 */
@Data
public class SysRolePageReq {
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 当前页码
     */
    private Long current = 1L;

    /**
     * 每页数量
     */
    private Long size = 10L;
}