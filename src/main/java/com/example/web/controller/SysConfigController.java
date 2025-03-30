package com.example.web.controller;

import com.example.common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/permission")
public class SysConfigController {
    @GetMapping("/routes")
    public Response routes() {
        return Response.success();
    }
}
