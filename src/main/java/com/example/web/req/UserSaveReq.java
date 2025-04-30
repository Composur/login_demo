package com.example.web.req;

import java.util.List; // 导入 List

public class UserSaveReq {
    private String username;
    private Integer isEnabled; // 对应 JSON 中的 isEnabled
    private List<String> roleIds; // 对应 JSON 中的 roleIds
    private String orgId; // 对应 JSON 中的 orgId
    private String nickname; // 对应 JSON 中的 nickname
    private String password; // 对应 JSON 中的 password
    private String confirmPassword; // 对应 JSON 中的 confirmPassword

    // --- Getters and Setters ---
    // (通常需要为所有字段添加 Getters 和 Setters，这里省略以保持简洁)
    // 你可以使用 IDE 自动生成它们

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
