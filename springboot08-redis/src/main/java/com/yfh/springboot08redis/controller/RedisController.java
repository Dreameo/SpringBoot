package com.yfh.springboot08redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/testRedis")
    public String testRedis() {
        // 设置值到redis
        redisTemplate.opsForValue().set("name", "lucy");
        // 从reids中获取值
        Object name = redisTemplate.opsForValue().get("name");
        return (String) name;
    }

}
