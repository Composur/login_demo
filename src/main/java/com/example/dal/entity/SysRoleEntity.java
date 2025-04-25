package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统角色实体类
 */
@Data
@TableName("sys_role") // 指定数据库表名
public class SysRoleEntity extends BaseEntity {

    /**
     * 角色名称
     */
    //@Column(name = "role_name", nullable = false, length = 30)
    private String roleName;

    /**
     * 角色编码
     */
    //@Column(name = "role_code", nullable = false, length = 20)
    private String roleCode;

    /**
     * 描述
     */
    //@Column(name = "description", length = 255)
    private String description;

    /**
     * 是否启用 (数据库字段 is_enable)
     * 注意：数据库字段名是 is_enable，Java 属性名通常用驼峰 enabled
     * 如果使用 MyBatis-Plus 等框架，可能需要配置映射或保持字段名一致
     */
    //@Column(name = "is_enable") // 明确指定列名
    private Integer enabled; // 对应数据库 int 类型，默认值 1 在数据库层面处理

    /**
     * 是否系统内置 (数据库字段 is_system)
     */
    //@Column(name = "is_system", nullable = false) // 明确指定列名
    private Integer isSystem; // 对应数据库 int 类型，默认值 0 在数据库层面处理

    // Lombok @Data 会自动生成 Getters, Setters, toString, equals, hashCode
}