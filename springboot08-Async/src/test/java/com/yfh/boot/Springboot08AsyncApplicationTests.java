package com.yfh.boot;

import com.alibaba.druid.pool.DruidDataSource;
import com.yfh.boot.pojo.User;
import com.yfh.boot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class Springboot08AsyncApplicationTests {
    @Autowired
    DataSource dataSource;


    @Test
    public void contextLoads() throws SQLException {
        //看一下默认数据源
        System.out.println(dataSource.getClass());
        //获得连接
        Connection connection =   dataSource.getConnection();
        System.out.println(connection);

        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        System.out.println("druidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
        System.out.println("druidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());

        //关闭连接
        connection.close();
    }

    @Autowired
    UserService userService;

    @Test
    public void testUserMapper() {
//        User user = userService.getUserById(1);
//        User user = userService.getUserById1(1);
//        User user = userService.getById(1);
//        System.out.println(user);
//        File file = new File("");
//        System.out.println(file);
    }

    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Test
    public void testMail() {
        // 简单邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("测试邮件：这是主题哟");
        mailMessage.setText("这是邮件的正文");

        mailMessage.setTo("fanghua8899@gmail.com");
        mailMessage.setFrom("yfh97@qq.com");
        javaMailSender.send(mailMessage);
    }


    @Test
    public void testMail1() throws MessagingException {
        // 复杂邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        //正文
        helper.setSubject("复杂邮件主题");
        helper.setText("<p style='color:red'>带有样式的复杂邮件正文</p>", true);

        //附件
        helper.addAttachment("1.jpg", new File("E:\\aaaaa.jpg"));
        helper.addAttachment("2.gif", new File("E:\\timg.gif"));


        helper.setTo("fanghua8899@gmail.com");
        helper.setFrom("yfh97@qq.com");

        javaMailSender.send(mimeMessage);
    }


}
