package com.yfh.springboot.springboot04web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/timg.jpg")
    public String imgTest(@PathVariable("h") String s) {
        return "hello Img";
    }

}
