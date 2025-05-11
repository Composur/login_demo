package com.example.web.req;

import lombok.Data;

@Data
public class PwdRestReq {
    private String confirmPassword;
    private String newPassword;
}
