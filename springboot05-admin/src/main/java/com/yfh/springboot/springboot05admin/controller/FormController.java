package com.yfh.springboot.springboot05admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@Slf4j
public class FormController {

    @GetMapping("/form_layouts")
    public String formLayout() {
        return "form/form_layouts";
    }

    /**
     * 文件上传
     *
     */

    @PostMapping("/upload")
    public String upload(HttpSession session, @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestPart("headImg")MultipartFile headImg,
                         @RequestPart("lifePhoto") MultipartFile[] lifePhoto) throws IOException {
        log.info("上传的信息: email={},password={},headImg={},lifePhoto={}",email,password, headImg, lifePhoto);

        // 获取服务器中上传文件夹uploads的路径
        ServletContext servletContext = session.getServletContext();
        String uploads = servletContext.getRealPath("uploads");
        File file = new File(uploads);
        if(!file.exists())
            file.mkdir();

        // 如果上传的文件不为空
        if (headImg != null) {
            String originalFilename = headImg.getOriginalFilename();// 获取上传文件的名字
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 后缀
            String fileName = UUID.randomUUID().toString() + suffix;
            String finalPath = uploads + File.separator + fileName;
            headImg.transferTo(new File(finalPath));
        }
        log.info(uploads);
        if (lifePhoto.length > 0) {
            for (MultipartFile photo : lifePhoto) {
                String originalFilename = photo.getOriginalFilename();// 获取上传文件的名字
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 后缀
                String fileName = UUID.randomUUID().toString() + suffix;
                String finalPath = uploads + File.separator + fileName;
                System.out.println(finalPath);
                photo.transferTo(new File(finalPath));
            }
        }


        return "main";
    }
}
