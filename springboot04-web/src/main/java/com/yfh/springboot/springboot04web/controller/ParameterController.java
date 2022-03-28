package com.yfh.springboot.springboot04web.controller;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ParameterController {

    @GetMapping("/car/{id}/owner/{username}")
    public Map<String, Object> getCar(@PathVariable("id") Integer id,
                                      @PathVariable("username") String username,
                                      @PathVariable Map<String, Object> pv,
                                      @RequestParam("age") Integer age,
                                      @RequestParam("inters") List<String> inters,
                                      @RequestParam Map<String, Object> params
//                                      @CookieValue("Idea-677c364") Cookie cookie
                                     ) {

        Map<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("username", username);
//        map.put("pv", pv);
        map.put("age", age);
        map.put("inters",inters);
        map.put("params", params);
//        System.out.println("cookie.getValue() = " + cookie.getValue());
        return map;
    }

}
