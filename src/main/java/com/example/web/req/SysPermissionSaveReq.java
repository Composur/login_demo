package com.example.web.req;

import lombok.Data;

/**
 * 系统权限保存请求
 */
@Data
public class SysPermissionSaveReq {
    protected String id;
    /**
     * 菜单类型
     */
    private Integer menuType;

    /**
     * 是否为外链
     */
    private Integer isFrame;


    /**
     * 是否显示链接
     */
    private Boolean showLink;


    /**
     * 是否启用
     */
    private Boolean enabled;


    /**
     * 是否保持活跃状态
     */
    private Boolean keepAlive;

    /**
     * 是否显示父级
     */
    private Boolean showParent;


    /**
     * 图标
     */
    private String icon;

    /**
     * 标题
     */
    private String title;

    /**
     * 路径
     */
    private String path;

    /**
     * 排序
     */
    private Integer rank;

    /**
     * 名称
     */
    private String name;

    /**
     * 组件
     */
    private String component;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 权限标识
     */
    private String perms;

}