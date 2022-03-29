package com.yfh.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/sql")
    public Long sql(){
        Long aLong = jdbcTemplate.queryForObject("select count(*) from t_user", Long.class);
        return aLong;
    }


    @RequestMapping("/hello")
    public String index() {
        return "hello world";
    }


}
