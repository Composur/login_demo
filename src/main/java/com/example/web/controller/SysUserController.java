package com.example.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Response;
import com.example.dal.entity.SysUserEntity;
import com.example.service.SysUserService;
import com.example.web.mapper.UserTransfer;
import com.example.web.req.PwdRestReq;
import com.example.web.req.UserQueryReq;
import com.example.web.req.UserSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysUserResp;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
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
     * 删除用户
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public Response<?> delete(@Validated @RequestBody Set<String> ids) {
        if (null == ids || ids.isEmpty()) {
            return Response.error("id 为空！");
        }
        sysUserService.deleteByIds(ids);
        //根据用户ID删除用户角色
        //sysUserService.deleteUserRoleByUserId(id);
        return Response.success("删除成功！");
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

    @PutMapping("/reset/passwd/{username}")
    public Response<?> resetPasswd(@PathVariable String username, @RequestBody PwdRestReq req) {
        if (StrUtil.isBlank(req.getNewPassword()) || StrUtil.isBlank(req.getConfirmPassword()) || !req.getNewPassword().equals(req.getConfirmPassword())) {
            return Response.error("两次密码不一致");
        }
        return Response.success(sysUserService.resetPasswd(username, req));
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
    public Response<PageResult<SysUserResp>> queryPage(UserQueryReq queryReq) { // 修改方法参数
        // 调用新的 Service 方法
        IPage<SysUserEntity> userEntityPage = sysUserService.queryUserPage(queryReq); // 传递 UserQueryReq 对象

        // 将 List<SysUserEntity> 转换为 List<SysUserResp>
        List<SysUserResp> sysUserResps = userEntityPage.getRecords().stream()
                .map(userEntity -> UserTransfer.INSTANCE.toSysUserResp(userEntity)) // 使用注入的 UserTransfer
                .collect(Collectors.toList());

        // 从 IPage 对象获取正确的分页信息构建 PageResult
        PageResult<SysUserResp> pageResult = new PageResult<>(
                sysUserResps,
                userEntityPage.getTotal(), // 获取总记录数
                userEntityPage.getCurrent(), // 获取当前页码
                userEntityPage.getSize()     // 获取每页数量
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
