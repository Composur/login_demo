package com.example.web.controller;

import com.example.common.Response;
import com.example.common.annotation.WebLog;
import com.example.security.token.RouteCacheProvider;
import com.example.service.CurrentUserService;
import com.example.service.SysPermissionService;
import com.example.service.dto.UserDTO;
import com.example.web.req.SysPermissionSaveReq;
import com.example.web.resp.PermissionRoutesResp;
import com.example.web.resp.SysUserMenuTreeResp;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/sys/permission")
@RequiredArgsConstructor
@Validated
public class SysPermissionController {
    private final SysPermissionService sysPermissionService;
    private final CurrentUserService currentUserService;
    private final RouteCacheProvider routeCache;


    /**
     * 保存
     *
     * @param req
     * @return
     */
    @PostMapping("/save")
    public Response save(@RequestBody SysPermissionSaveReq req) {
        // 这里可以添加保存逻辑
        sysPermissionService.savePermission(req);
        // TODO 新增的时候同步权限到 sys_api_permission
        // 方案一：权限管理页面支持接口信息录入
        // 方案二：接口权限和菜单权限分开维护
        // 方案三：后端自动扫描接口生成权限（适合大项目）

        return Response.success("保存成功");
    }

    /**
     * 更新
     *
     * @param id
     * @param req
     * @return
     */
    @WebLog("菜单编辑")
    @PutMapping("/update")
    public Response update(@RequestParam String id, @RequestBody SysPermissionSaveReq req) {
        // 这里可以添加更新逻辑
        sysPermissionService.updatePermission(id, req);
        return Response.success("更新成功");
    }

    @DeleteMapping("/delete")
    public Response delete(@RequestParam @NotBlank(message = "id不能为空") String id) {
        sysPermissionService.deleteById(id);
        return Response.success("删除成功");
    }

    /**
     * 获取用户路由
     *
     * @return
     */
    @GetMapping("/routes")
    public Response routes() {
        UserDTO user = currentUserService.getCurrentUser();
        //缓存
        Optional<List<PermissionRoutesResp>> routeCacheOptional = routeCache.getRoutes(user.getId());
        if (routeCacheOptional.isPresent()) {
            return Response.success(routeCacheOptional.get());
        }
        List<PermissionRoutesResp> menuRoute = null;
        if (user.isManger()) {
            Set<String> permissionIds = sysPermissionService.allPermissionIds();
            menuRoute = sysPermissionService.queryRouteByIds(new ArrayList<>(permissionIds));
        } else {
            menuRoute = sysPermissionService.queryRouteByUserid(user.getId());
        }
        routeCache.cacheRoutes(user.getId(), menuRoute);
        return Response.success(menuRoute);
    }

    /**
     * 获取菜单
     *
     * @return
     */
    @GetMapping("/tree")
    public Response<List<SysUserMenuTreeResp>> tree() {
        return getMenuTreeResponse();
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping("/tree/menu")
    public Response<List<SysUserMenuTreeResp>> treeMenu() {
        return getMenuTreeResponse();
    }

    /**
     * 获取菜单树响应
     *
     * @return 菜单树响应
     */
    private Response<List<SysUserMenuTreeResp>> getMenuTreeResponse() {
        List<SysUserMenuTreeResp> menuTree = sysPermissionService.queryMenuTree();
        return Response.success(menuTree);
    }
}
