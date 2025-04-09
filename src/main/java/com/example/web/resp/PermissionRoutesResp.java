package com.example.web.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PermissionRoutesResp {
    /**
     * 路由地址
     */
    private String path;
    /**
     * 路由名字（必须保持唯一）
     */
    private String name;
    /**
     * 按需加载需要展示的页面
     */
    private String component;
    /**
     * 路由元信息
     */
    private VueMenuRouteMeta meta;
    /**
     * 菜单
     */
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PermissionRoutesResp> children;
}


