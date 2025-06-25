package com.example.web.req;

import lombok.Data;

import java.util.List;

@Data
public class UserSaveReq {
    private String id;
    private String username;
    private Integer isEnabled; // 是否启用（建议和enabled统一）
    private Boolean enabled; // 是否启用（布尔型，和前端enabled字段对应）
    private List<String> roleIds; // 角色ID列表
    private String orgId; // 组织ID
    private String orgCode; // 组织编码
    private String orgName; // 组织名称
    private String nickname; // 昵称
    private String password; // 密码
    private String confirmPassword; // 确认密码
    private String phone; // 手机号
    private String email; // 邮箱
    private Integer isManage; // 是否管理员（0/1）
}
