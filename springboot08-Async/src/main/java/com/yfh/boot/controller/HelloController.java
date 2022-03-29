package com.yfh.boot.controller;

import com.yfh.boot.pojo.User;
import com.yfh.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/sql")
    public Long sql() {
        Long aLong = jdbcTemplate.queryForObject("select count(*) from t_user", Long.class);
        return aLong;
    }


    @RequestMapping("/hello")
    public String index() {
        return "hello world";
    }

    @GetMapping("/user")
    public User getUserById(Integer id) {
//        User user = userService.getUserById(id);
        User user = userService.getById(id);
        return user;
    }

}
