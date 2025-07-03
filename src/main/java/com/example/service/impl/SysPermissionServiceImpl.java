package com.example.service.impl;

import com.example.common.enums.MenuType;
import com.example.dal.entity.SysPermissionEntity;
import com.example.dal.mapper.SysPermissionMapper;
import com.example.dal.mapper.SysUserRoleMapper;
import com.example.service.SysPermissionService;
import com.example.web.mapper.SysPermissionTransfer;
import com.example.web.req.SysPermissionSaveReq;
import com.example.web.resp.PermissionRoutesResp;
import com.example.web.resp.SysUserMenuTreeResp;
import com.example.web.resp.VueMenuRouteMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysPermissionMapper sysPermissionMapper;
    private final SysUserRoleMapper sysUserRoleMapper;


    /**
     * 获取所有权限编码
     *
     * @return 所有权限编码
     */
    public Set<String> allPermissionCodes() {
        List<SysPermissionEntity> permissions = sysPermissionMapper.listPermission(null, null);
        if (permissions == null) {
            return Collections.emptySet();
        }
        return permissions.stream().map(SysPermissionEntity::getPerms).collect(Collectors.toSet());
    }

    /**
     * @param ids
     * @return
     */
    public Set<String> listPermissionCodesByPermissionIds(List<String> ids) {
        if (ids.isEmpty()) {
            return Collections.emptySet();
        }
        List<SysPermissionEntity> permissions = sysPermissionMapper.listPermission(ids, null);
        if (permissions != null) {
            return permissions.stream().map(p -> p.getPerms()).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public Set<String> listPermissionIdsByRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<SysPermissionEntity> permissions = sysPermissionMapper.listPermissionIdsByRoleIds(roleIds, null);
        if (permissions != null) {
            return permissions.stream().map(SysPermissionEntity::getId).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public Set<String> allPermissionIds() {
        // 查询前端菜单
        List<SysPermissionEntity> permissions = sysPermissionMapper.list(null, null);
        if (permissions == null) {
            return Collections.emptySet();
        }
        return permissions.stream()
                .map(permission -> String.valueOf(permission.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public List<PermissionRoutesResp> queryRouteByIds(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysPermissionEntity> permissions = sysPermissionMapper.listMenuByIds(userIds, true);
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为树形结构
        return buildRoutesTree(permissions);
    }

    /**
     * 查询用户权限对应的路由树
     *
     * @param userId 用户ID
     * @return 路由树列表
     */
    @Override
    public List<PermissionRoutesResp> queryRouteByUserid(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<SysPermissionEntity> permissions = sysPermissionMapper.listMenuByUserId(userId, true);
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptyList();
        }
        // 转换为树形结构
        return buildRoutesTree(permissions);
    }


    @Override
    public List<SysUserMenuTreeResp> queryMenuTree() {
        List<SysPermissionEntity> permissions = sysPermissionMapper.list(null, null);
        if (permissions != null) {
            // 转换为树形结构
            return buildMenuTree(permissions);
        }
        return List.of();
    }

    /**
     * @param req 权限菜单请求对象
     * @return
     */
    /**
     * 保存权限菜单信息
     *
     * @param req 权限菜单请求对象
     * @return 保存后的权限实体对象
     */
    @Override
    public SysPermissionEntity savePermission(SysPermissionSaveReq req) {
        // 参数校验
        if (req == null) {
            throw new IllegalArgumentException("权限菜单请求对象不能为空");
        }

        // 转换请求对象为实体对象
        SysPermissionEntity entity = SysPermissionTransfer.INSTANCE.toSysPermissionEntity(req);

        // 判断是新增还是更新
        if (entity.getId() == null || entity.getId().isEmpty()) {
            // 新增操作 - 不需要手动设置ID、创建时间等，MyBatis Plus会自动处理
            sysPermissionMapper.insert(entity);
        } else {
            // 更新操作 - 先查询是否存在
            SysPermissionEntity existingEntity = sysPermissionMapper.selectById(entity.getId());
            if (existingEntity == null) {
                throw new RuntimeException("要更新的权限菜单不存在，ID: " + entity.getId());
            }

            // 执行更新操作 - 不需要手动设置更新时间，MyBatis Plus会自动处理
            sysPermissionMapper.updateById(entity);
        }

        // 返回保存后的实体
        return entity;
    }

    /**
     * @param id
     * @param req 权限菜单请求对象
     * @return
     */
    @Override
    public SysPermissionEntity updatePermission(String id, SysPermissionSaveReq req) {
        return savePermission(req);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        if (sysPermissionMapper.deleteById(id) > 0) {
            return 1;
        }
        return 0;
    }

    /**
     * @param id 角色id
     * @return
     */
    @Override
    public List<String> getUserIdsByRoleId(String id) {
        return sysUserRoleMapper.getUserIdsByRoleId(id);
    }


    // 新增辅助方法：构建 SysUserMenuTreeResp 结构的树
    private List<SysUserMenuTreeResp> buildMenuTree(List<SysPermissionEntity> permissions) {
        // 复用已有的分组和根节点查找逻辑
        Map<String, List<SysPermissionEntity>> parentIdMap = groupPermissionsByParentId(permissions);
        List<SysPermissionEntity> rootMenus = filterAndSortRootMenus(permissions);

        return rootMenus.stream()
                .map(menu -> convertToMenuTreeResp(menu, parentIdMap))
                .collect(Collectors.toList());
    }

    //将 SysPermissionEntity 转换为 SysUserMenuTreeResp
    private SysUserMenuTreeResp convertToMenuTreeResp(SysPermissionEntity menu,
                                                      Map<String, List<SysPermissionEntity>> parentIdMap) {
        SysUserMenuTreeResp node = SysPermissionTransfer.INSTANCE.toSysUserMenuTreeResp(menu);

        // 根据菜单类型设置菜单类型名称
        if (menu.getMenuType() != null) {
            node.setMenuTypeName(MenuType.getNameByCode(menu.getMenuType()));
        }


        String menuId = menu.getId();
        List<SysPermissionEntity> childrenEntities = parentIdMap.get(menuId);
        if (childrenEntities != null && !childrenEntities.isEmpty()) {
            // 子节点排序
            childrenEntities.sort(java.util.Comparator.comparingInt(SysPermissionEntity::getRank));
            node.setChildren(childrenEntities.stream()
                    .map(child -> convertToMenuTreeResp(child, parentIdMap))
                    .collect(Collectors.toList()));
        } else {
            // 确保 SysUserMenuTreeResp 类中有 setChildren 方法
            node.setChildren(Collections.emptyList());
        }
        return node;
    }


    /**
     * 构建路由树
     *
     * @param permissions 权限列表
     * @return 路由树列表
     */
    private List<PermissionRoutesResp> buildRoutesTree(List<SysPermissionEntity> permissions) {
        // 按父ID分组
        Map<String, List<SysPermissionEntity>> parentIdMap = groupPermissionsByParentId(permissions);

        // 找到并排序根菜单
        List<SysPermissionEntity> rootMenus = filterAndSortRootMenus(permissions);

        // 构建树结构
        return rootMenus.stream()
                .map(menu -> convertToRouteResp(menu, parentIdMap, true))
                .collect(Collectors.toList());
    }

    /**
     * 将权限列表按父ID分组
     *
     * @param permissions 权限列表
     * @return 按父ID分组的Map，根节点的父ID为"root"
     */
    private Map<String, List<SysPermissionEntity>> groupPermissionsByParentId(List<SysPermissionEntity> permissions) {
        return permissions.stream()
                .collect(Collectors.groupingBy(menu -> {
                    String parentId = menu.getParentId();
                    // 将顶层菜单的父ID统一处理为"root"，便于查找
                    return (parentId == null || parentId.isEmpty()) ? "root" : parentId;
                }));
    }

    /**
     * 过滤并排序根菜单
     *
     * @param permissions 权限列表
     * @return 排序后的根菜单列表
     */
    private List<SysPermissionEntity> filterAndSortRootMenus(List<SysPermissionEntity> permissions) {
        return permissions.stream()
                // 筛选出父ID为空的作为根菜单
                .filter(menu -> menu.getParentId() == null || menu.getParentId().isEmpty())
                // 按rank字段升序排序
                .sorted(java.util.Comparator.comparingInt(SysPermissionEntity::getRank))
                .collect(Collectors.toList());
    }

    /**
     * 将权限实体转换为路由响应对象（递归构建子节点）
     *
     * @param menu        当前菜单实体
     * @param parentIdMap 按父ID分组的权限Map
     * @param isRoot      是否为根节点
     * @return 路由响应对象
     */
    private PermissionRoutesResp convertToRouteResp(SysPermissionEntity menu,
                                                    Map<String, List<SysPermissionEntity>> parentIdMap,
                                                    boolean isRoot) {
        PermissionRoutesResp route = new PermissionRoutesResp();
        String path = addLeadingSlash(menu.getPath());
        route.setPath(path);
        route.setName(menu.getName());
        // 处理 component，根路由通常有固定布局组件
        String component = menu.getComponent();
        route.setComponent(component);


        VueMenuRouteMeta meta = new VueMenuRouteMeta();
        meta.setTitle(menu.getTitle());
        meta.setIcon(menu.getIcon());
        meta.setShowLink(menu.getShowLink() == 1);

        if (isRoot) {
            meta.setRank(menu.getRank());
        } else {
            meta.setKeepAlive(menu.getKeepAlive() == 1);
            // 非根节点通常需要显示父级，除非特殊配置
            meta.setShowParent(true);
        }
        route.setMeta(meta);

        // 递归获取子节点
        String menuId = menu.getId();
        List<SysPermissionEntity> children = parentIdMap.get(menuId);
        if (children != null && !children.isEmpty()) {
            // 子节点排序
            children.sort(java.util.Comparator.comparingInt(SysPermissionEntity::getRank));
            route.setChildren(children.stream()
                    .map(child -> convertToRouteResp(child, parentIdMap, false)) // 子节点isRoot为false
                    .collect(Collectors.toList()));
        }

        return route;
    }

    /**
     * 为路径添加前导斜杠（如果需要）
     *
     * @param path 路径
     * @return 处理后的路径
     */
    private String addLeadingSlash(String path) {
        if (path != null && !path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }
}
