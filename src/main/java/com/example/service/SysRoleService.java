package com.example.service;

import com.example.service.dto.RoleQueryDTO;
import com.example.web.resp.SysRoleResp;

import java.util.List;

public interface SysRoleService {
    /**
     * 保存角色
     */
    String save();

    /**
     * 查询角色列表（兼容原有方法）
     * 根据当前用户权限自动判断返回全部角色或仅用户拥有的角色
     */
    List<SysRoleResp> queryList();
    
    /**
     * 查询全部角色列表（仅管理员可用）
     */
    List<SysRoleResp> queryAllRoles();
    
    /**
     * 查询当前用户拥有的角色列表
     */
    List<SysRoleResp> queryOwnRoles();
    
    /**
     * 根据查询条件获取角色列表
     * @param queryDTO 查询条件
     * @return 角色列表
     */
    List<SysRoleResp> queryRolesByCondition(RoleQueryDTO queryDTO);
}
