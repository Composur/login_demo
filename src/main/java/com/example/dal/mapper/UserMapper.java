package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("INSERT INTO user (id, username, password) VALUES (#{id}, #{username}, #{password})")
    int insert(User user);

    User getUserByUsername(@Param("username") String username);
}
