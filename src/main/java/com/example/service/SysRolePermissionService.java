package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dal.entity.SysRolePermissionEntity;

import java.util.List;

public interface SysRolePermissionService extends IService<SysRolePermissionEntity> {
    boolean grantPermission(String roleId, List<String> permissionIds);
}
