package com.yfh.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yfh.boot.mapper")
public class Springboot07SwaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot07SwaggerApplication.class, args);

    }

}
