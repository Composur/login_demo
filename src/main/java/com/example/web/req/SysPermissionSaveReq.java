package com.example.web.req;

import lombok.Data;

/**
 * 系统权限保存请求
 */
@Data
public class SysPermissionSaveReq {
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

    public Integer getShowLink() {
        return showLink == null ? 0 : (showLink ? 1 : 0);
    }


    /**
     * 是否启用
     */
    private Boolean enabled;

    public Integer getEnabled() {
        return enabled == null ? 0 : (enabled ? 1 : 0);
    }


    /**
     * 是否保持活跃状态
     */
    private Boolean keepAlive;

    public Integer getKeepAlive() {
        return keepAlive == null ? 0 : (keepAlive ? 1 : 0);
    }

    /**
     * 是否显示父级
     */
    private Boolean showParent;

    public Integer getShowParent() {
        return showParent == null ? 0 : (showParent ? 1 : 0);
    }


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

}