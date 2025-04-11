package com.example.service;

import com.example.web.resp.PermissionRoutesResp;

import java.util.List;
import java.util.Set;

public interface SysPermissionService {

    Set<String> allPermissionCodes();

    Set<String> allPermissionIds();

    List<PermissionRoutesResp> queryRouteByIds(List<String> userId);
}
