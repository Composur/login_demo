package com.example.web.controller;

import com.example.common.Response;
import com.example.common.ResponseCode;
import com.example.common.annotation.WebLog;
import com.example.dal.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/public")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public Response index() {
        return Response.success();
    }

    @GetMapping("/register")
    public Response registerPage() {
        return Response.success("register");
    }

    @WebLog("用户注册接口")
    @PostMapping("/register")
    @ResponseBody
    public Response register(@RequestBody User user) {
        if (userService.register(user)) {
            return Response.success("注册成功");
        }
        return Response.error(ResponseCode.BAD_REQUEST, "注册失败，用户名已存在", null);
    }

    @PostMapping("/login")
    @ResponseBody
    public Response login(String username, String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return Response.success("登录成功");
        }
        return Response.error(ResponseCode.BAD_REQUEST,"登录失败，用户名或密码错误", null);
    }
}