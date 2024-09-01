package com.atguigu.netty.learn.netty.EventLoop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author 123
 * @create 2024-09-01-19:02
 */
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        Channel channel = new Bootstrap()
                //2.添加 EventLoop
                .group(new NioEventLoopGroup())
                //3.选择客户端的 NIO 线程，
                .channel(NioSocketChannel.class)
                //4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //5.连接服务器
                .connect(new InetSocketAddress("127.0.0.1", 8080))
                .sync()
                .channel();
        System.out.println(channel);
        System.out.println("");
    }
}
