package com.atguigu.netty.learn.base.thread01;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static com.atguigu.netty.learn.utils.ByteBufferUtil.debugAll;

/**
 * @author 123
 * @create 2024-08-31-23:51
 */
@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i]  = new Worker("worker-" + i);
        }
        AtomicInteger index = new AtomicInteger();
        while (true){
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    log.debug("connected...{}",sc.getRemoteAddress());
                    //关联selector
                    log.debug("before register...{}",sc.getRemoteAddress());
                    workers[index.getAndIncrement()%workers.length].register(sc);//boss线程调用 初始化 selector ,启动 worker-01
                    log.debug("after register...{}",sc.getRemoteAddress());
                }
            }
        }
    }
    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();
        private boolean start = false;
        public Worker(String name){
            this.name = name;
        }
        public void register(SocketChannel sc) throws IOException {
            if (!start){
                selector = Selector.open();
                thread = new Thread(this,name);
                thread.start();
                start = true;
            }
            //提交任务
            queue.add(() -> {
                try {
                    sc.register(selector,SelectionKey.OP_READ,null);//boss
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true){
                try {
                    selector.select();//worker-01
                    //提交任务
                    Runnable task = queue.poll();
                    if(task != null){
                        task.run();//注册
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()){
                        SelectionKey key = iter.next();
                        iter.remove();
                        if(key.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("read...{}",channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
