package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data          // Lombok 注解，自动生成 getter/setter、toString 等方法
@TableName("user") // 映射到数据库表 "user"
public class User {
    @TableId(type = IdType.AUTO) // 主键策略：由数据库自动生成（如自增）
    private Long id;
    private String username;     // 用户名
    private String password;     // 密码
    private String email;        // 邮箱
}