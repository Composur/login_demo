package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@TableName("sys_user")
public class SysUserEntity extends BaseEntity {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 所属机构
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 机构code
     */
    //@Schema(description = "机构code")
    private String orgCode;
    /**
     * 是否网点管理员
     */
    private Integer isManage;


    public boolean isManager() {
        return isManage == 1;
    }
}