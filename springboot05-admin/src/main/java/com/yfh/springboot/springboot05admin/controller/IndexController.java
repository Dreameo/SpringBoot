package com.yfh.springboot.springboot05admin.controller;

import com.yfh.springboot.springboot05admin.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    /**
     * 去登录页
     * @return
     */
    @GetMapping(value = {"/", "/login"})
    public String toLoginPage () {
        return "login";
    }

    /**
     * 登录操作
     * @return
     */
    @PostMapping("/login")
    public String login(User user, HttpSession session, Model model) {

        if (!StringUtils.isEmpty(user.getUserName()) && StringUtils.hasLength(user.getPassword())) {
            session.setAttribute("user", user); // 存入缓存
            return "redirect:/main.html"; // 登录成功重定向到main页面，防止表单重复提交
        }
        model.addAttribute("msg", "用户名或者密码错误"); // 请求域中存放数据
        return "login";
    }

    /**
     * 去主页面
     * @return
     */
    @GetMapping("/main.html")
    public String toMainPage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user != null) {
            return "main"; // 只有登录用户才可以进这个页面,没登录的返回登录页面
        }
        model.addAttribute("msg", "还没有登录呢");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
