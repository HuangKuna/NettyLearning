package com.atguigu.netty.learn.netty.EventLoop;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author 123
 * @create 2024-09-01-15:45
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        //处理普通任务
        group.next().submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("ok");
        });
        log.debug("main");
        //处理定时任务
        group.next().scheduleAtFixedRate(()->{
            log.debug("sch---ok");
        },0,1, TimeUnit.SECONDS);
        log.debug("main");
    }
}
