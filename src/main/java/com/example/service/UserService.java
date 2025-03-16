package com.example.service;

import com.example.entity.User;

public interface UserService {
    boolean register(User user);
    User login(String username, String password);
}