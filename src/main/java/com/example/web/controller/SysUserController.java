package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysUserService;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.UserTransfer;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysUserResp;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/query/page")
    public Response<PageResult<SysUserResp>> queryList() {
        List<UserDTO> list = sysUserService.queryUserList();
        // 将 SysUserResp 的创建移到 map 操作内部
        //List<SysUserResp> sysUserResps = list.stream().map(userDTO -> {
        //    //SysUserResp sysUserResp = new SysUserResp(); // 在 lambda 内部创建新对象
        //    //sysUserResp.setId(userDTO.getId());
        //    //sysUserResp.setUsername(userDTO.getUsername());
        //    // 如果 SysUserResp 还有其他字段，也需要从 userDTO 映射过来
        //    // sysUserResp.setNickname(userDTO.getNickname());
        //    //return sysUserResp;
        //    return UserTransfer.INSTANCE.toSysUserResp(userDTO);
        //}).collect(Collectors.toList());
        List<SysUserResp> sysUserResps = UserTransfer.INSTANCE.toSysUserRespList(list);
        PageResult<SysUserResp> pageResult = new PageResult<>(sysUserResps, sysUserResps.size(), 1, 10);
        return Response.success(pageResult);
    }
}
