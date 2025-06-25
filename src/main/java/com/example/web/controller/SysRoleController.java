package com.example.web.controller;

import com.example.common.BusinessException;
import com.example.common.Response;
import com.example.service.SysRolePermissionService;
import com.example.service.SysRoleService;
import com.example.service.dto.RoleQueryDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.req.SysRolePageReq;
import com.example.web.req.SysRoleSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysRoleResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("sys/role")
public class SysRoleController {

    @Autowired
    private final SysRoleService sysRoleService;

    private final SysRolePermissionService sysRolePermissionService;

    /**
     * 分页查询角色列表
     *
     * @return
     */
    @GetMapping("/query/page")
    public Response<PageResult<SysRoleResp>> queryPage(SysRolePageReq req) {
        RoleQueryDTO queryDTO = SysRoleTransfer.INSTANCE.toRoleQueryDTO(req);
        PageResult<SysRoleResp> pageResult = sysRoleService.queryRolesByPage(queryDTO);
        return Response.success(pageResult);
    }

    /**
     * 根据用户查询角色列表
     *
     * @return
     */
    @GetMapping("/query/list")
    public Response<List<SysRoleResp>> queryList() {
        // 创建一个空的查询对象，用于查询所有数据
        RoleQueryDTO queryDTO = new RoleQueryDTO();
        List<SysRoleResp> sysRoleResps = sysRoleService.queryList(queryDTO);
        return Response.success(sysRoleResps);
    }


    /**
     * 检查角色编码是否有效
     *
     * @param code 待验证的代码字符串
     * @return 返回一个Response对象，其中包含一个布尔值，表示代码是否有效
     */
    @GetMapping("/check/code")
    public Response<Boolean> checkCode(String code) {
        return Response.success(sysRoleService.checkCode(code));
    }


    /**
     * 保存角色信息
     *
     * @param req 角色保存请求参数
     * @return 响应结果
     */
    @PostMapping("/save")
    public Response<String> save(@RequestBody SysRoleSaveReq req) {
        return Response.success(sysRoleService.save(req));
    }

    /**
     * 更新角色信息
     *
     * @param req 角色更新请求参数
     * @return 响应结果
     */
    @PutMapping("/update")
    public Response<String> update(@RequestParam String id, @RequestBody SysRoleSaveReq req) {
        return Response.success(sysRoleService.update(id, req));
    }

    /**
     * 删除角色信息
     *
     * @param id 角色ID
     * @return 响应结果
     */
    @DeleteMapping("/delete")
    public Response<String> delete(@RequestParam String id) {
        return Response.success(sysRoleService.delete(id));
    }

    /**
     * 查询某个角色已授权的权限集合
     *
     * @param id 角色ID
     * @return 响应结果
     */
    @GetMapping("/permission")
    public Response<List<String>> queryPermissions(@RequestParam String id) {
        return Response.success(sysRolePermissionService.getPermissionIdByRoleId(id));
    }

    /**
     * 为某个角色授予权限
     *
     * @param id            角色ID
     * @param permissionIds 权限ID集合
     * @return 响应结果
     */
    @PutMapping("/permission")
    public Response<String> grantPermission(
            @RequestParam String id,
            @RequestBody List<String> permissionIds) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }

        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new IllegalArgumentException("权限ID列表不能为空");
        }
        boolean result = sysRoleService.grantPermission(id, permissionIds);
        if (!result) {
            throw new BusinessException("权限更新未生效");
        }
        return Response.success();
    }

}
