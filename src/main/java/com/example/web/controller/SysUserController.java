package com.example.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Response;
import com.example.dal.entity.SysUserEntity;
import com.example.service.SysUserService;
import com.example.web.mapper.UserTransfer;
import com.example.web.req.UserSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysUserResp;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    //private final UserTransfer userTransfer; // 注入 UserTransfer

    /**
     * 保存用户
     *
     * @param req
     * @return
     */
    @PostMapping("/save")
    public Response<?> save(@RequestBody UserSaveReq req) {
        return sysUserService.save(req);
    }

    /**
     * 修改用户
     *
     * @param req
     * @return
     */
    @PutMapping("/update/{id}")
    public Response<?> update(@PathVariable String id, @RequestBody UserSaveReq req) {
        return sysUserService.update(req);
    }

    /**
     * 检查用户名是否可用
     *
     * @param username
     * @return
     */
    @GetMapping("/check/username")
    public Response<Boolean> checkUsername(@RequestParam String username) {
        return Response.success(sysUserService.checkUsername(username));
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping("/query/page")
    public Response<PageResult<SysUserResp>> queryPage(@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "1") int current) { // 修改方法名，添加默认值
        // 调用新的 Service 方法
        IPage<SysUserEntity> userDtoPage = sysUserService.queryUserPage(size, current);

        // 将 List<UserDTO> 转换为 List<SysUserResp>
        List<SysUserResp> sysUserResps = userDtoPage.getRecords().stream()
                .map(userDTO -> UserTransfer.INSTANCE.toSysUserResp(userDTO)) // 使用注入的 UserTransfer
                .collect(Collectors.toList());

        // 从 IPage 对象获取正确的分页信息构建 PageResult
        PageResult<SysUserResp> pageResult = new PageResult<>(
                sysUserResps,
                userDtoPage.getTotal(), // 获取总记录数
                userDtoPage.getCurrent(), // 获取当前页码
                userDtoPage.getSize()     // 获取每页数量
        );
        return Response.success(pageResult);
    }

    /**
     * 查询用户角色ID
     *
     * @param id
     * @return
     */
    @GetMapping("/query/role/ids")
    public Response<Set<String>> queryRoleIdsByUserId(@RequestParam String id) {
        return Response.success(sysUserService.queryRoleIdsByUserId(id));
    }
}
