# SpringBoot-Security

登录失败要关闭csrf功能：
 // 防止攻击工具：get，post
        http.csrf().disable();//关闭csrf功能，登录失败肯定存在的原因


## SpringSecurity重要的两个部分：认证和授权
用户认证（Authentication）和用户授权（Authorization）
