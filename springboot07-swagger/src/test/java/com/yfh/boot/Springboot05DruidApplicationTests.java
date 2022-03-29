package com.yfh.boot;

import com.alibaba.druid.pool.DruidDataSource;
import com.yfh.boot.pojo.User;
import com.yfh.boot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class Springboot05DruidApplicationTests {
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
        User user = userService.getUserById(1);
        System.out.println(user);
    }

}
