package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dal.entity.SysRolePermissionEntity;
import com.example.dal.mapper.SysRolePermissionMapper;
import com.example.service.SysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermissionEntity> implements SysRolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 授权权限
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    @Override
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

        return this.saveBatch(rolePermissionEntities);
    }

}
