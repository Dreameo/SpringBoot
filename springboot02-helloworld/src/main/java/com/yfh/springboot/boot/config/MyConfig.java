package com.yfh.springboot.boot.config;

import com.sun.deploy.ref.Helpers;
import com.yfh.springboot.boot.bean.Pet;
import com.yfh.springboot.boot.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.logging.Filter;

/**
 * 1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
 * 2、配置类本身也是组件
 * 3、proxyBeanMethods：代理bean的方法
 *      Full(proxyBeanMethods=true)、【保证每个@Bean方法被调用多少次返回的组件都是单实例的】
 *      Lite(proxyBeanMethods=false)【每个@Bean方法被调用多少次返回的组件都是新创建的】
 *      组件依赖必须使用Full模式默认。其他默认是否Lite模式
 */


@Import({User.class, Pet.class, Helpers.class})
@Configuration(proxyBeanMethods = false)
//@ConditionalOnBean(name = "tompet")
public class MyConfig {
//    Full:外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象

    @Bean // 给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user1() {
        User zhangsan = new User("zhangsan", 19);
        zhangsan.setPet(pet()); // proxyBeanMethods = false 但是不影响运行！！

        /**
         * user组件依赖了Pet组件
         */

        return zhangsan;
    }

    @Bean("tompet")
    public Pet pet() {
        return new Pet("petty");
    }
}
