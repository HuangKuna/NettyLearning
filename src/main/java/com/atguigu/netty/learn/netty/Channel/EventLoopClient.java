package com.atguigu.netty.learn.netty.Channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author 123
 * @create 2024-09-01-19:02
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        ChannelFuture channelFuture = new Bootstrap()
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
                // 异步非阻塞 main发起调用，真正执行connect是nio线程
                .connect(new InetSocketAddress("127.0.0.1", 8080));
      /*
        2.1 使用sync 方法同步处理结果
      // 使用sync方法，阻塞等待，知道连接建立
        channelFuture.sync();
        // 无阻塞
        Channel channel = channelFuture.channel();
        channel.writeAndFlush("hello,world");*/

        //2.2 使用addListener(回调对象)方法 异步处理结果

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在nio线程链接成功后会执行此方法，会调用operationComplete方法
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                log.debug("{}",channel);
                channel.writeAndFlush("hello,word");
            }
        });

    }
}
