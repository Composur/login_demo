package com.example.service;

import com.example.dal.entity.SysPermission;
import com.example.dal.mapper.SysPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysPermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    public Set<String> allPermissionCodes() {
        List<SysPermission> permissions = sysPermissionMapper.listPermission(null, null);
        if (permissions == null) {
            return Collections.emptySet();
        }
        return permissions.stream().map(SysPermission::getPerms).collect(Collectors.toSet());
    }
}
