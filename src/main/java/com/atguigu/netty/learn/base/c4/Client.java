package com.atguigu.netty.learn.base.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author 123
 * @create 2024-08-30-22:41
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8888));
        System.out.println("waitting for server");
    }
}
