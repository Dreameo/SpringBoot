package com.yfh.boot.service.impl;

import com.yfh.boot.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {
    //告诉Spring这是一个异步方法
    @Async
    @Override
    public void chat() {
        System.out.println("chat ... 业务运行");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
