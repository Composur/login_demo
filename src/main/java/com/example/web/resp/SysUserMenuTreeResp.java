package com.example.web.resp;

import lombok.Data;

import java.util.List;

@Data
/**
 * 系统用户菜单树响应类
 */
public class SysUserMenuTreeResp {

    /**
     * 菜单ID
     */
    private String id;

    /**
     * 父菜单ID
     */
    private String parentId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 是否缓存
     */
    private Boolean keepAlive;

    /**
     * 是否显示
     */
    private Boolean showLink;

    /**
     * 菜单类型名称
     */
    private String menuTypeName;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 子菜单列表
     */
    private List<SysUserMenuTreeResp> children;
}
