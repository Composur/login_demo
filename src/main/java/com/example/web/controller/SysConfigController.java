package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysConfigService;
import com.example.service.dto.SysConfigDTO;
import com.example.web.mapper.SysConfigTransfer;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysConfigPageResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/config")
public class SysConfigController {
    private final SysConfigService sysConfigService;

    /**
     * 系统配置查询
     *
     * @param req 分页查询请求对象，包含查询参数和分页信息
     * @return 返回查询结果的状态，"success"表示查询成功
     */
    @GetMapping("/query/page")
    public Response<PageResult<SysConfigPageResp>> queryPage(SysConfigPageReq req) {
        PageResult<SysConfigDTO> dtoPage = sysConfigService.queryConfigByPage(req);
        PageResult<SysConfigPageResp> respPage = SysConfigTransfer.INSTANCE.toRespPage(dtoPage);
        return Response.success(respPage);
    }
}
