package com.example.service;

import com.example.web.resp.PermissionRoutesResp;
import com.example.web.resp.SysUserMenuTreeResp;

import java.util.List;
import java.util.Set;

public interface SysPermissionService {

    Set<String> allPermissionCodes();

    //
    Set<String> listPermissionByRoleIds(List<String> roleIds);

    // 根据角色id查询权限id
    Set<String> listPermissionIdsByRoleIds(List<String> roleIds);

    Set<String> allPermissionIds();

    List<PermissionRoutesResp> queryRouteByIds(List<String> userId);

    List<PermissionRoutesResp> queryRouteByUserid(String userId);

    // 查询菜单树
    List<SysUserMenuTreeResp> queryMenuTree();
}
