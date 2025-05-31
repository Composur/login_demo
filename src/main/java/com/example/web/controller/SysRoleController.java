package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysRoleService;
import com.example.service.dto.RoleQueryDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.req.SysRolePageReq;
import com.example.web.req.SysRoleSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysRoleResp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("sys/role")
public class SysRoleController {

    @Autowired
    private final SysRoleService sysRoleService;

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
}
