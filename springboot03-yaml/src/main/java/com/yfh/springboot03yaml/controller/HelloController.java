package com.yfh.springboot03yaml.controller;

import com.yfh.springboot03yaml.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private Person person;

    @RequestMapping("/index")
    public String index() {
        return "Hello SpringBoot2, Index page!!!";
    }

    @RequestMapping("/person")
    public Person person() {
        return person;
    }
}
