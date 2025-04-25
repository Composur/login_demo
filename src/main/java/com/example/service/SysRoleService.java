package com.example.service;


import com.example.web.resp.SysRoleResp;

import java.util.List;

public interface SysRoleService {
    String save();

    List<SysRoleResp> queryList();
}
