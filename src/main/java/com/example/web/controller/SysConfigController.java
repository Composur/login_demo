package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysConfigService;
import com.example.service.dto.SysConfigDTO;
import com.example.web.mapper.SysConfigTransfer;
import com.example.web.req.SysConfigSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysConfigPageResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/config")
public class SysConfigController {
    private final SysConfigService sysConfigService;

    /**
     * 查询系统配置列表
     */
    @GetMapping("/query/page")
    public Response<PageResult<SysConfigPageResp>> queryPage(SysConfigPageReq req) {
        PageResult<SysConfigDTO> dtoPage = sysConfigService.queryConfigByPage(req);
        PageResult<SysConfigPageResp> respPage = SysConfigTransfer.INSTANCE.toRespPage(dtoPage);
        return Response.success(respPage);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public Response<SysConfigDTO> save(@RequestBody @Valid SysConfigSaveReq req) {
        SysConfigDTO sysConfigDTO = SysConfigTransfer.INSTANCE.toSysConfigDTO(req);
        SysConfigDTO save = sysConfigService.save(sysConfigDTO);
        return Response.success(save);
    }

    /**
     * 更新
     */
    @PutMapping("/update/{id}")
    public Response<SysConfigDTO> update(@PathVariable String id, @RequestBody @Valid SysConfigSaveReq req) {
        SysConfigDTO sysConfigDTO = SysConfigTransfer.INSTANCE.toSysConfigDTO(req);
        SysConfigDTO sysConfigDTONew = sysConfigService.update(id, sysConfigDTO);
        return Response.success(sysConfigDTONew);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete")
    public Response<?> delete(@RequestBody Set<String> ids) {
        sysConfigService.deleteByIds(ids);
        return Response.success("删除成功");
    }

    /**
     * 刷新缓存
     */
    @PutMapping("/refresh/{id}")
    public Response<?> refreshCache(@PathVariable String id) {
        sysConfigService.refreshCache(id);
        return Response.success("刷新成功");
    }

    /**
     * 刷新全量缓存
     */
    @PostMapping("/refresh")
    public Response<?> refreshCache() {
        sysConfigService.refreshAllCache();
        return Response.success("刷新成功");
    }
}
