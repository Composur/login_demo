package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysRoleService;
import com.example.service.dto.RoleQueryDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.req.SysRolePageReq;
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

    @PostMapping
    public String save() {
        return sysRoleService.save();
    }
}
