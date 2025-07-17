package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    public boolean hasPermission(String username, String url, String method) {
        // TODO: 权限校验逻辑
        // 1. 根据url和method查数据库/缓存，获取所需权限
        // 2. 查询当前用户拥有的权限
        // 3. 判断用户是否有问权限
        // 返回true/false
        return true;
    }
}