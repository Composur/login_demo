package com.example.web.controller;

import com.example.common.Response;
import com.example.security.utils.SecurityUtil;
import com.example.service.dto.UserDTO;
import com.example.web.resp.PermissionRoutesResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/sys/permission")
public class SysConfigController {
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
            //Set<String> permissionIds = sysPermissionService.allPermissionIds();
            //menuRoute = sysPermissionService.queryRouteByIds(List.copyOf(permissionIds));
        } else {
            //menuRoute = sysPermissionService.queryRouteByUserid(user.getUserid());
        }
        //routeCache.putCache(user.getUserid(), menuRoute);
        List list = Collections.emptyList();
        return Response.success(list);
    }
}
