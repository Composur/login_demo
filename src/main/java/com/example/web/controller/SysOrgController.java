package com.example.web.controller;

import com.example.common.Response;
import com.example.service.SysOrgService;
import com.example.service.dto.OrgListItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/org")
@RequiredArgsConstructor
public class SysOrgController {

    private final SysOrgService sysOrgService;

    // 获取机构列表
    @GetMapping("/query/list")
    public Response queryOrgList(@RequestParam String isEnable) {
        System.out.println(isEnable);
        List<OrgListItemDTO> orgList = sysOrgService.getOrgList();
        return Response.success(orgList);
    }

}
