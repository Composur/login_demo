package com.example.web.controller;

import com.example.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("sys/role")
public class SysController {

    @Autowired
    private final SysRoleService sysRoleService;


    @PostMapping
    public String save() {
        return sysRoleService.save();
    }
}
