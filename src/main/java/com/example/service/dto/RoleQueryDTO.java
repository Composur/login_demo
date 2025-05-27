package com.example.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色查询参数DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleQueryDTO {
    /**
     * 是否只查询当前用户拥有的角色
     */
    private boolean onlyOwnRoles;

    /**
     * 指定角色编码列表进行查询，为空则不限制
     */
    private List<String> roleCodes;

    /**
     * 角色名称（模糊查询），为空则不限制
     */
    private String roleName;

    /**
     * 是否启用，为null则不限制
     */
    private Integer enabled;
}