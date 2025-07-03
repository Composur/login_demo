package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统用户角色实体类
 * 对应数据库表 sys_user_role
 */
@Data
@TableName("sys_user_role")
public class SysUserRoleEntity {
    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;
}