package com.example.security.token;

import com.example.web.resp.PermissionRoutesResp;

import java.util.List;
import java.util.Optional;

public interface RouteCacheProvider {
    boolean cacheRoutes(String userId, List<PermissionRoutesResp> routes);

    Optional<List<PermissionRoutesResp>> getRoutes(String userId);

    boolean clearRoutes(String userId);
}