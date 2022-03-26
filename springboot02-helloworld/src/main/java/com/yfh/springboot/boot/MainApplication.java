package com.yfh.springboot.boot;

import com.yfh.springboot.boot.bean.Pet;
import com.yfh.springboot.boot.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * 自动配置原理：
 *      @SpringBootApplication是
 *          @SpringBootConfiguration
 *          @EnableAutoConfiguration
 *          @ComponentScan 这三个注解的合成注解
 */


@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        // 1. 返回IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        // 2. 查看容器里面的组件
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String bean : beanDefinitionNames) {
            System.out.println(bean);
        }

        // 3. 从容器中获取组件
        /**
         * 默认单例对象，因此不管多少次获取组件，都是从容器中获取同一个。
         */
        User user1 = run.getBean("user1", User.class);
        User user2 = run.getBean("user1", User.class);
        System.out.println(user1==user2);

        /**
         * 但是SpringBoot2在@Configuration注解中添加了proxyBeanMethods属性。
         * 如果@Configuration(proxyBeanMethods = true)代理对象调用方法。SpringBoot总会检查这个组件是否在容器中有。
         *
         */
        User user01 = run.getBean("user1", User.class);
        Pet tom = run.getBean("tompet", Pet.class);

        System.out.println("用户的宠物："+(user01.getPet() == tom));


        // 获取组件
        String[] beanNamesForType = run.getBeanNamesForType(User.class);
        System.out.println("============User=================");
        for (String s : beanNamesForType) {
            System.out.println(s);
        }

        String[] petBeanForTypes = run.getBeanNamesForType(Pet.class);
        System.out.println("============Pet=================");
        for (String s : petBeanForTypes) {
            System.out.println(s);
        }

    }
}