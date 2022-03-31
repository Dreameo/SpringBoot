package com.yfh.boot.controller;

import com.yfh.boot.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/sayHello")
    public String sayHello() throws InterruptedException {
        asyncService.chat();
        return "Hello World";
    }


}
