package com.yfh.boot.service.impl;

import com.yfh.boot.mapper.UserMapper;
import com.yfh.boot.pojo.User;
import com.yfh.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }
}
