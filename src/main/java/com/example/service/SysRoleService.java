package com.example.service;

import com.example.service.dto.RoleQueryDTO;
import com.example.web.req.SysRoleSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysRoleResp;

import java.util.List;

public interface SysRoleService {
    /**
     * 保存角色
     */
    String save(SysRoleSaveReq req);

    /**
     * 更新角色
     */
    String update(String id, SysRoleSaveReq req);

    /**
     * 查询角色列表（兼容原有方法）RoleQueryDTO queryDTO
     * 根据当前用户权限自动判断返回全部角色或仅用户拥有的角色
     */
    List<SysRoleResp> queryList(RoleQueryDTO queryDTO);


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
     *
     * @param queryDTO 查询条件
     * @return 角色列表
     */
    List<SysRoleResp> queryRolesByCondition(RoleQueryDTO queryDTO);

    /**
     * 分页查询角色列表
     *
     * @param queryDTO 查询条件（包含分页参数）
     * @return 分页结果
     */
    PageResult<SysRoleResp> queryRolesByPage(RoleQueryDTO queryDTO);

    /**
     * 校验角色编码是否存在
     *
     * @param code 角色编码
     * @return true：存在，false：不存在
     */
    Boolean checkCode(String code);


    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 删除结果
     */
    String delete(String id);
}
