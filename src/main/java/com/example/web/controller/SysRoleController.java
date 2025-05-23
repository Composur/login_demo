package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysRoleService;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysRoleResp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response<PageResult<SysRoleResp>> queryPage() {
        List<SysRoleResp> sysRoleResps = sysRoleService.queryList();
        PageResult<SysRoleResp> pageResult = new PageResult<>(sysRoleResps, 1, 1, 1);
        return Response.success(pageResult);
    }

    /**
     * 根据用户查询角色列表
     *
     * @return
     */
    @GetMapping("/query/list")
    public Response<List<SysRoleResp>> queryList() {
        List<SysRoleResp> sysRoleResps = sysRoleService.queryList();
        return Response.success(sysRoleResps);
    }

    @PostMapping
    public String save() {
        return sysRoleService.save();
    }
}
