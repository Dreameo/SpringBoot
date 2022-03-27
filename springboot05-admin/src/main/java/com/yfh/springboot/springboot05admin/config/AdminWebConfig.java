package com.yfh.springboot.springboot05admin.config;

import com.yfh.springboot.springboot05admin.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 所有要定制实现web功能的配置都要实现WebMvcConfigurer
 */


@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 链式结构
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login","/css/**", "/fonts/**", "/images/**", "/js/**");
    }
}
