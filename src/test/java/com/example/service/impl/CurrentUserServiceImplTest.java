package com.example.service.impl;

import com.example.common.BusinessException;
import com.example.security.token.UserCacheProvider;
import com.example.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CurrentUserServiceImpl 单元测试类
 * 
 * 注意：由于 SecurityUtil.getCurrentUsername() 是静态方法，
 * 我们通过 Mock SecurityContextHolder 来间接测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("当前用户服务测试")
class CurrentUserServiceImplTest {

    @Mock
    private UserCacheProvider userCacheProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CurrentUserServiceImpl currentUserService;

    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testUser = new UserDTO();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setNickname("测试用户");
        testUser.setRoles(Arrays.asList("ADMIN", "USER"));
        testUser.setPermissions(Arrays.asList("user:read", "user:write"));
        testUser.setManger(true);
        testUser.setOrgId("org001");
    }

    @Test
    @DisplayName("获取当前用户 - 用户已登录")
    void getCurrentUser_WhenUserLoggedIn_ShouldReturnUser() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            UserDTO result = currentUserService.getCurrentUser();

            // Then - 验证结果
            assertNotNull(result);
            assertEquals("user123", result.getId());
            assertEquals("testuser", result.getUsername());
            assertEquals("测试用户", result.getNickname());
            assertTrue(result.isManger());
            assertEquals("org001", result.getOrgId());
            
            // 验证方法调用
            verify(userCacheProvider, times(1)).getUser(username);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户 - 用户未登录")
    void getCurrentUser_WhenUserNotLoggedIn_ShouldThrowBusinessException() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回空
        when(userCacheProvider.getUser(username)).thenReturn(Optional.empty());

        try {
            // When & Then - 执行测试方法并验证异常
            BusinessException exception = assertThrows(BusinessException.class, 
                () -> currentUserService.getCurrentUser());
            
            assertEquals("用户未登录", exception.getMessage());
            
            // 验证方法调用
            verify(userCacheProvider, times(1)).getUser(username);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户可选信息 - 用户已登录")
    void getCurrentUserOptional_WhenUserLoggedIn_ShouldReturnUser() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            Optional<UserDTO> result = currentUserService.getCurrentUserOptional();

            // Then - 验证结果
            assertTrue(result.isPresent());
            assertEquals("user123", result.get().getId());
            assertEquals("testuser", result.get().getUsername());
            
            // 验证方法调用
            verify(userCacheProvider, times(1)).getUser(username);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户可选信息 - 用户未登录")
    void getCurrentUserOptional_WhenUserNotLoggedIn_ShouldReturnEmpty() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回空
        when(userCacheProvider.getUser(username)).thenReturn(Optional.empty());

        try {
            // When - 执行测试方法
            Optional<UserDTO> result = currentUserService.getCurrentUserOptional();

            // Then - 验证结果
            assertFalse(result.isPresent());
            
            // 验证方法调用
            verify(userCacheProvider, times(1)).getUser(username);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户可选信息 - SecurityContext为空")
    void getCurrentUserOptional_WhenSecurityContextIsNull_ShouldReturnEmpty() {
        // Given - 准备测试数据：SecurityContext为空
        SecurityContextHolder.clearContext();

        // When - 执行测试方法
        Optional<UserDTO> result = currentUserService.getCurrentUserOptional();

        // Then - 验证结果
        assertFalse(result.isPresent());
        
        // 验证方法调用
        verify(userCacheProvider, never()).getUser(anyString());
    }

    @Test
    @DisplayName("获取当前用户可选信息 - Authentication为空")
    void getCurrentUserOptional_WhenAuthenticationIsNull_ShouldReturnEmpty() {
        // Given - 准备测试数据：Authentication为空
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        try {
            // When - 执行测试方法
            Optional<UserDTO> result = currentUserService.getCurrentUserOptional();

            // Then - 验证结果
            assertFalse(result.isPresent());
            
            // 验证方法调用
            verify(userCacheProvider, never()).getUser(anyString());
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户ID - 用户已登录")
    void getCurrentUserId_WhenUserLoggedIn_ShouldReturnUserId() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            Optional<String> result = currentUserService.getCurrentUserId();

            // Then - 验证结果
            assertTrue(result.isPresent());
            assertEquals("user123", result.get());
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户ID - 用户未登录")
    void getCurrentUserId_WhenUserNotLoggedIn_ShouldReturnEmpty() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回空
        when(userCacheProvider.getUser(username)).thenReturn(Optional.empty());

        try {
            // When - 执行测试方法
            Optional<String> result = currentUserService.getCurrentUserId();

            // Then - 验证结果
            assertFalse(result.isPresent());
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户名 - 用户已登录")
    void getCurrentUsername_WhenUserLoggedIn_ShouldReturnUsername() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        try {
            // When - 执行测试方法
            Optional<String> result = currentUserService.getCurrentUsername();

            // Then - 验证结果
            assertTrue(result.isPresent());
            assertEquals("testuser", result.get());
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("获取当前用户名 - 用户未登录")
    void getCurrentUsername_WhenUserNotLoggedIn_ShouldReturnEmpty() {
        // Given - 准备测试数据：SecurityContext为空
        SecurityContextHolder.clearContext();

        // When - 执行测试方法
        Optional<String> result = currentUserService.getCurrentUsername();

        // Then - 验证结果
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("判断当前用户是否为管理员 - 是管理员")
    void isCurrentUserManager_WhenUserIsManager_ShouldReturnTrue() {
        // Given - 准备测试数据
        String username = "testuser";
        testUser.setManger(true);
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            boolean result = currentUserService.isCurrentUserManager();

            // Then - 验证结果
            assertTrue(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断当前用户是否为管理员 - 不是管理员")
    void isCurrentUserManager_WhenUserIsNotManager_ShouldReturnFalse() {
        // Given - 准备测试数据
        String username = "testuser";
        testUser.setManger(false);
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            boolean result = currentUserService.isCurrentUserManager();

            // Then - 验证结果
            assertFalse(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断当前用户是否为管理员 - 用户未登录")
    void isCurrentUserManager_WhenUserNotLoggedIn_ShouldReturnFalse() {
        // Given - 准备测试数据
        String username = "testuser";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回空
        when(userCacheProvider.getUser(username)).thenReturn(Optional.empty());

        try {
            // When - 执行测试方法
            boolean result = currentUserService.isCurrentUserManager();

            // Then - 验证结果
            assertFalse(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断用户是否拥有指定角色 - 拥有该角色")
    void hasRole_WhenUserHasRole_ShouldReturnTrue() {
        // Given - 准备测试数据
        String username = "testuser";
        String roleCode = "ADMIN";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            boolean result = currentUserService.hasRole(roleCode);

            // Then - 验证结果
            assertTrue(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断用户是否拥有指定角色 - 不拥有该角色")
    void hasRole_WhenUserDoesNotHaveRole_ShouldReturnFalse() {
        // Given - 准备测试数据
        String username = "testuser";
        String roleCode = "SUPER_ADMIN";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            boolean result = currentUserService.hasRole(roleCode);

            // Then - 验证结果
            assertFalse(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断用户是否拥有指定角色 - 角色编码为null")
    void hasRole_WhenRoleCodeIsNull_ShouldReturnFalse() {
        // When - 执行测试方法
        boolean result = currentUserService.hasRole(null);

        // Then - 验证结果
        assertFalse(result);
        
        // 验证没有调用任何依赖方法
        verify(userCacheProvider, never()).getUser(anyString());
    }

    @Test
    @DisplayName("判断用户是否拥有指定角色 - 用户未登录")
    void hasRole_WhenUserNotLoggedIn_ShouldReturnFalse() {
        // Given - 准备测试数据
        String username = "testuser";
        String roleCode = "ADMIN";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回空
        when(userCacheProvider.getUser(username)).thenReturn(Optional.empty());

        try {
            // When - 执行测试方法
            boolean result = currentUserService.hasRole(roleCode);

            // Then - 验证结果
            assertFalse(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断用户是否拥有指定权限 - 拥有该权限")
    void hasPermission_WhenUserHasPermission_ShouldReturnTrue() {
        // Given - 准备测试数据
        String username = "testuser";
        String permissionCode = "user:read";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            boolean result = currentUserService.hasPermission(permissionCode);

            // Then - 验证结果
            assertTrue(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断用户是否拥有指定权限 - 不拥有该权限")
    void hasPermission_WhenUserDoesNotHavePermission_ShouldReturnFalse() {
        // Given - 准备测试数据
        String username = "testuser";
        String permissionCode = "user:delete";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回用户信息
        when(userCacheProvider.getUser(username)).thenReturn(Optional.of(testUser));

        try {
            // When - 执行测试方法
            boolean result = currentUserService.hasPermission(permissionCode);

            // Then - 验证结果
            assertFalse(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    @DisplayName("判断用户是否拥有指定权限 - 权限编码为null")
    void hasPermission_WhenPermissionCodeIsNull_ShouldReturnFalse() {
        // When - 执行测试方法
        boolean result = currentUserService.hasPermission(null);

        // Then - 验证结果
        assertFalse(result);
        
        // 验证没有调用任何依赖方法
        verify(userCacheProvider, never()).getUser(anyString());
    }

    @Test
    @DisplayName("判断用户是否拥有指定权限 - 用户未登录")
    void hasPermission_WhenUserNotLoggedIn_ShouldReturnFalse() {
        // Given - 准备测试数据
        String username = "testuser";
        String permissionCode = "user:read";
        
        // Mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        
        // Mock UserCacheProvider.getUser() 返回空
        when(userCacheProvider.getUser(username)).thenReturn(Optional.empty());

        try {
            // When - 执行测试方法
            boolean result = currentUserService.hasPermission(permissionCode);

            // Then - 验证结果
            assertFalse(result);
        } finally {
            // 清理 SecurityContext
            SecurityContextHolder.clearContext();
        }
    }
} 