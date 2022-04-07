package com.yfh.springboot08redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @GetMapping("testLock")
    public void testLock(){
        //1获取锁，setne
//        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "111");
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
        //2获取锁成功、查询num的值
        if(lock){
            Object value = redisTemplate.opsForValue().get("num");
            System.out.println("value" + value);
            //2.1判断num为空return
            if(StringUtils.isEmpty(value)){
                return;
            }
            //2.2有值就转成成int
            int num = Integer.parseInt(value+"");
            //2.3把redis的num加1
            redisTemplate.opsForValue().set("num", ++num);


            //2.4释放锁，del

            Object lock_uuid = redisTemplate.opsForValue().get("lock");
            if (lock_uuid == uuid)
                redisTemplate.delete("lock");

        }else{
            //3获取锁失败、每隔0.1秒再获取
            try {
                Thread.sleep(100);
                testLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
