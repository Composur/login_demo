package com.example.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.Response;
import com.example.service.SysUserService;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.UserTransfer;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysUserResp;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    //private final UserTransfer userTransfer; // 注入 UserTransfer
    @GetMapping("/check/username")
    public Response<Boolean> checkUsername(@RequestParam String username) {
        return Response.success(sysUserService.checkUsername(username));
    }

    @GetMapping("/query/page")
    public Response<PageResult<SysUserResp>> queryPage(@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "1") int current) { // 修改方法名，添加默认值
        // 调用新的 Service 方法
        IPage<UserDTO> userDtoPage = sysUserService.queryUserPage(size, current);

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
}
