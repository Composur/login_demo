package com.example.service;

import com.example.dal.entity.SysPermissionEntity;
import com.example.web.req.SysPermissionSaveReq;
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

    /**
     * 保存权限菜单信息
     *
     * @param req 权限菜单请求对象
     * @return 保存后的权限实体对象
     */
    SysPermissionEntity savePermission(SysPermissionSaveReq req);
}
