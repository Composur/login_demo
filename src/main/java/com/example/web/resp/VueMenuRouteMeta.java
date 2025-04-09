package com.example.web.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 菜单路由 mate
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2023/2/6
 */
@Data
@EqualsAndHashCode
public class VueMenuRouteMeta implements Serializable {
    /**
     * title
     */
    private String title;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 是否显示该菜单
     */
    private Boolean showLink;
    /**
     * 菜单排序，值越高排的越后（只针对顶级路由）
     */
    private Integer rank;
    /**
     * 是否显示父级菜单
     */
    private Boolean showParent;
    /**
     * 是否缓存该路由页面（开启后，会保存该页面的整体状态，刷新后会清空状态）
     */
    private Boolean keepAlive;
    /**
     * 需要内嵌的iframe链接地址
     */
    private String frameSrc;
}
