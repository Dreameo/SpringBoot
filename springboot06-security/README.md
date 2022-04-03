# SpringBoot-Security

登录失败要关闭csrf功能：
 // 防止攻击工具：get，post
        http.csrf().disable();//关闭csrf功能，登录失败肯定存在的原因

