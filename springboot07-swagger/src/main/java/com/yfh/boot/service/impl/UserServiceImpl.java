package com.yfh.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfh.boot.mapper.UserMapper;
import com.yfh.boot.pojo.User;
import com.yfh.boot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
//
//    @Autowired
//    UserMapper userMapper;
//
//    @Override
//    public User getUserById(Integer id) {
//        return userMapper.getUserById(id);
//    }
//
//    @Override
//    public User getUserById1(Integer id) {
//        return userMapper.getUserById1(id);
//    }
}
