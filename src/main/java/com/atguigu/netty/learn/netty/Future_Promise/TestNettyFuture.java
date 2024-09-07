package com.atguigu.netty.learn.netty.Future_Promise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author 123
 * @create 2024-09-01-22:02
 */
@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                log.debug("执行计算任务");
                Thread.sleep(2000);
                return 50;
            }
        });
        log.debug("等待结果");
        //同步
//      System.out.println(future.get());

        //异步
        future.addListener(future1 -> {
            log.debug("异步获取结果");
            System.out.println(future1.getNow());
        });
    }
}
