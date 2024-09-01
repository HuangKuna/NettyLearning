package com.atguigu.netty.learn.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author 123
 * // 程序的主入口
 */
public class HelloServer {
    public static void main(String[] args) {
        // 创建服务器启动助手
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                //1。创建 NioEventLoopGroup，可以简单理解为 `线程池 + Selector` 后面会详细展开
                .channel(NioServerSocketChannel.class)
                //2.选择服务 Scoket 实现类，其中 NioServerSocketChannel 表示基于 NIO 的服务器端实现，其它实现还有
                .childHandler(
                 //3.为啥方法叫 childHandler，是接下来添加的处理器都是给 SocketChannel 用的，而不是给 ServerSocketChannel。
                 // ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel 以便添加更多的处理器
                    new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 向管道添加StringDecoder，用于将接收到的数据解码为字符串
                        ch.pipeline().addLast(new StringDecoder());//5.SocketChannel 的处理器，解码 ByteBuf => String
                        // 向管道添加自定义的ChannelInboundHandlerAdapter，用于处理通道事件
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){//6.SocketChannel 的业务处理器，使用上一个处理器的处理结果
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 当通道有数据读取时，打印接收到的消息
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(8080);//4.ServerSocketChannel 绑定的监听端口
    }

}