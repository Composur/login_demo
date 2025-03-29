package com.example.web.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class LoginResp implements Serializable {
    private String username;
    private String nickname;
    private List<String> permissions;
    private Set<String> roles;
    private String token;
}