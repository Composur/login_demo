package com.example.web.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用户查询请求参数类
 */
public class UserQueryReq {
    /**
     * 每页显示记录数,默认10条
     */
    private int size = 10; // 默认每页数量
    
    /**
     * 当前页码,默认第1页
     */
    private int current = 1; // 默认当前页码
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 账号启用状态
     * null: 未指定状态
     * 0: 禁用
     * 1: 启用
     */
    private Integer enable; // 使用 Integer 以便区分未传值(null)和传0的情况
    
    /**
     * 组织机构ID
     */
    private String orgId;
}