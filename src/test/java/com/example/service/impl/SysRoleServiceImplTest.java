package com.example.service.impl;

import com.example.service.SysRolePermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysRoleServiceImplTest {

    @InjectMocks
    private SysRoleServiceImpl sysRoleService;

    @Mock
    private SysRolePermissionService sysRolePermissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void grantPermission_WithValidInput_ShouldReturnTrue() {
        // Arrange
        String roleId = "role1";
        List<String> permissionIds = Arrays.asList("perm1", "perm2");
        when(sysRolePermissionService.grantPermission(roleId, permissionIds)).thenReturn(true);

        // Act
        boolean result = sysRoleService.grantPermission(roleId, permissionIds);

        // Assert
        assertTrue(result);
        verify(sysRolePermissionService).grantPermission(roleId, permissionIds);
    }

    @Test
    void grantPermission_WithNullRoleId_ShouldThrowException() {
        // Arrange
        List<String> permissionIds = Arrays.asList("perm1", "perm2");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sysRoleService.grantPermission(null, permissionIds));
        assertEquals("角色ID不能为空", exception.getMessage());
        verifyNoInteractions(sysRolePermissionService);
    }

    @Test
    void grantPermission_WithEmptyRoleId_ShouldThrowException() {
        // Arrange
        List<String> permissionIds = Arrays.asList("perm1", "perm2");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sysRoleService.grantPermission("", permissionIds));
        assertEquals("角色ID不能为空", exception.getMessage());
        verifyNoInteractions(sysRolePermissionService);
    }

    @Test
    void grantPermission_WithNullPermissionIds_ShouldThrowException() {
        // Arrange
        String roleId = "role1";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sysRoleService.grantPermission(roleId, null));
        assertEquals("权限ID列表不合法", exception.getMessage());
        verifyNoInteractions(sysRolePermissionService);
    }

    @Test
    void grantPermission_WithEmptyPermissionIds_ShouldThrowException() {
        // Arrange
        String roleId = "role1";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sysRoleService.grantPermission(roleId, Collections.emptyList()));
        assertEquals("权限ID列表不合法", exception.getMessage());
        verifyNoInteractions(sysRolePermissionService);
    }
}