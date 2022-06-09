# SpringBoot-Security

登录失败要关闭csrf功能：
 // 防止攻击工具：get，post
        http.csrf().disable();//关闭csrf功能，登录失败肯定存在的原因


## SpringSecurity重要的两个部分：认证和授权
用户认证（Authentication）和用户授权（Authorization）
![image](https://user-images.githubusercontent.com/49993189/172891201-325e0b47-7f2e-4bf1-b625-7fd66dadf828.png)

过程： UsernamePasswordAuthenticationFilter对用户名和密码信息进行校验  -》 BasiAuthenticationFilter进行继续判断 -》 经过校验后添加标志信息，表示经过了校验，最终经过FilterSecurityInterceptor做最后的判断（这里主要保存了我们配置的认证信息）

https://www.bilibili.com/read/cv12401446
