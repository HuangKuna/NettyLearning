package com.atguigu.netty.learn.netty.Future_Promise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author 123
 * @create 2024-09-01-21:58
 */
@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算任务");
                Thread.sleep(2000);
                return 5;
            }
        });
        //get 会阻塞
        log.debug("等待结果");
        System.out.println(future.get());
    }
}
