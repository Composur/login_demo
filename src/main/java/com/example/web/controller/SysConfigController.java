package com.example.web.controller;

import com.example.common.Response;
import com.example.security.utils.SecurityUtil;
import com.example.service.SysPermissionService;
import com.example.service.dto.UserDTO;
import com.example.web.resp.PermissionRoutesResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sys/permission")
@RequiredArgsConstructor
public class SysConfigController {
    private final SysPermissionService sysPermissionService;

    @GetMapping("/routes")
    public Response routes() {
        UserDTO user = SecurityUtil.getCurrentUser();
        //缓存
        //Optional<List<VueMenuRouteVO>> routeCacheOptional = routeCache.getCache(user.getUserid());
        //if (routeCacheOptional.isPresent()) {
        //    return R.OK(routeCacheOptional.get());
        //}
        List<PermissionRoutesResp> menuRoute = null;
        if (user.isManger()) {
            Set<String> permissionIds = sysPermissionService.allPermissionIds();
            menuRoute = sysPermissionService.queryRouteByIds(new ArrayList<>(permissionIds));
        } else {
            menuRoute = sysPermissionService.queryRouteByUserid(user.getId());
        }
        //routeCache.putCache(user.getUserid(), menuRoute);
        return Response.success(menuRoute);
    }
}
