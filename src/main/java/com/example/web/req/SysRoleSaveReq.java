package com.example.web.req;

import lombok.Data;

@Data
public class SysRoleSaveReq {
    private String roleCode;
    private String roleName;
    private String description;
    private Integer enabled;
}