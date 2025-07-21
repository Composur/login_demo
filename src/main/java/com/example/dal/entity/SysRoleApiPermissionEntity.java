package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色接口权限关联实体类
 * 对应数据库表 sys_role_api_permission
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_api_permission")
public class SysRoleApiPermissionEntity extends BaseEntity {

    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 接口权限ID
     */
    @TableField("api_permission_id")
    private String apiPermissionId;
}