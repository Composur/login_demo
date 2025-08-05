package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.config.cache.RedisCacheManager;
import com.example.dal.entity.SysRolePermissionEntity;
import com.example.dal.mapper.SysRolePermissionMapper;
import com.example.dal.mapper.SysUserRoleMapper;
import com.example.security.token.JwtTokenRedisCacheProvider;
import com.example.security.token.RouteCacheProvider;
import com.example.service.SysRolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermissionEntity> implements SysRolePermissionService {

    private final SysRolePermissionMapper sysRolePermissionMapper;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final RouteCacheProvider routeCache;

    private final JwtTokenRedisCacheProvider jwtTokenRedisCacheProvider;

    private final RedisCacheManager redisCache;

    /**
     * 授权权限
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    @Override
    @Transactional // 添加事务注解
    public boolean grantPermission(String roleId, List<String> permissionIds) {
        // 1. 根据角色ID删除已有的权限关联
        sysRolePermissionMapper.deleteByMap(new HashMap<String, Object>() {{
            put("role_id", roleId);
        }});
        List<SysRolePermissionEntity> rolePermissionEntities = new ArrayList<>();
        for (String permissionId : permissionIds) {
            SysRolePermissionEntity rolePermissionEntity = new SysRolePermissionEntity();
            rolePermissionEntity.setRoleId(roleId);
            rolePermissionEntity.setPermissionId(permissionId);
            rolePermissionEntities.add(rolePermissionEntity);
        }
        // 1. 查询该角色下所有用户ID
        List<String> userIds = sysUserRoleMapper.getUserIdsByRoleId(roleId);
        // 2. 批量清理这些用户的缓存
        for (String userId : userIds) {
            routeCache.clearRoutes(userId);
            // 删除用户所有token，强制重新登录
            String userTokensKey = jwtTokenRedisCacheProvider.getUserTokensKey(userId);
            Set<String> tokens = redisCache.smembers(userTokensKey);
            int removedCount = 0;
            if (tokens != null && !tokens.isEmpty()) {
                for (String token : tokens) {
                    if (jwtTokenRedisCacheProvider.removeToken(token)) {
                        removedCount++;
                    }
                }
                redisCache.del(userTokensKey);
            }
            log.info("用户 {} 的 {} 个token已被删除，强制重新登录", userId, removedCount);
        }
        return this.saveBatch(rolePermissionEntities);
    }

    /**
     * @param roleId
     * @return
     */
    @Override
    public List<String> getPermissionIdByRoleId(String roleId) {
        return sysRolePermissionMapper.getPermissionIdByRoleId(roleId);
    }

}
