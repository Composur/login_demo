package com.example.web.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

// Add this annotation to the class
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionRoutesResp {
    private String path;
    private String name;
    private String component;
    private VueMenuRouteMeta meta;
    private List<PermissionRoutesResp> children;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public VueMenuRouteMeta getMeta() {
        return meta;
    }

    public void setMeta(VueMenuRouteMeta meta) {
        this.meta = meta;
    }

    public List<PermissionRoutesResp> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionRoutesResp> children) {
        this.children = children;
    }
}


