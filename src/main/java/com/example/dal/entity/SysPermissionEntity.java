package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 菜单与权限
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
@ToString
public class SysPermissionEntity extends BaseEntity {
    /**
     * 父类
     */
    //@Schema(description = "父ID")
    private String parentId;
    /**
     * 路由地址
     */
    //@Schema(description = "路由地址")
    private String path;
    /**
     * 路由名字
     */
    //@Schema(description = "路由名称")
    private String name;
    /**
     * 菜单名称
     */
    //@Schema(description = "菜单名称")
    private String title;
    /**
     * 菜单icon
     */
    //@Schema(description = "菜单icon")
    private String icon;
    /**
     * 是否显示
     */
    //@Schema(description = "是否显示")
    private Integer showLink;

    public Integer getShowLink() {
        return showLink == null ? 0 : showLink;
    }

    /**
     * 排序
     */
    //@Schema(description = "排序")
    @TableField(value = "`rank`")
    private Integer rank;
    /**
     * 组件
     */
    //@Schema(description = "组件地址")
    private String component;
    /**
     * 是否显示父菜单
     */
    //@Schema(description = "是否显示父菜单")
    private Integer showParent;

    public Integer getShowParent() {
        return showParent == null ? 0 : showParent;
    }

    /**
     * 是否缓存该路由页面（开启后，会保存该页面的整体状态，刷新后会清空状态）
     */
    //@Schema(description = "是否缓存该路由页面")
    private Integer keepAlive;

    public Integer getKeepAlive() {
        return keepAlive == null ? 0 : keepAlive;
    }

    /**
     * 是否iframe
     */
    //@Schema(description = "是否iframe")
    private Integer isFrame;

    public Integer getIsFrame() {
        return isFrame == null ? 0 : isFrame;
    }

    /**
     * 类型（0：目录；1：菜单 ；2：按钮权限）
     */
    //@Schema(description = "类型")
    private Integer menuType;
    /**
     * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
     */
    //@Schema(description = "菜单权限编码")
    private String perms;
    /**
     * 是否启用
     */
    //@Schema(description = "是否启用")
    @TableField("is_enable")
    private Integer enabled;
}
