package com.atguigu.netty.learn.netty.Future_Promise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author 123
 * @create 2024-09-01-22:17
 */
@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 创建一个 EventLoop 对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
        // 2. 创建一个 Promise 对象，结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(() -> {
            // 3. 在子线程中设置promise的结果
                try {
                    Thread.sleep(1000);
                    promise.setSuccess(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    promise.setFailure(e);
                }
                }).start();
        log.debug("等待结果");
        log.debug("结果是 {}", promise.get());
    }
}
