package com.atguigu.netty.learn.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

/**
 * @author 123
 * @create 2024-09-01-15:02
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                //1.创建 NioEventLoopGroup，同 Server
                .channel(NioSocketChannel.class)
                //2.选择客户 Socket 实现类，NioSocketChannel 表示基于 NIO 的客户端实现，其它实现还有
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    //3.添加 SocketChannel 的处理器，ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel 以便添加更多的处理器
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }

                })
                .connect("127.0.0.1", 8080)//指定要连接的服务器和端口
                .sync()//5.Netty 中很多方法都是异步的，如 connect，这时需要使用 sync 方法等待 connect 建立连接完毕
                .channel()//6.获取 channel 对象，它即为通道抽象，可以进行数据读写操作
                .writeAndFlush(new Date() + ":hello world!");
    }
}
