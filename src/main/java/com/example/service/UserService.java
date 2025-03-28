package com.example.service;

import com.example.dal.entity.User;

public interface UserService {
    boolean register(User user);

    User login(String username, String password);

    User getUserById(Integer id);

    User getUserByUsername(String username);
}