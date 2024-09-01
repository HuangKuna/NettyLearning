package com.atguigu.netty.learn.base.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.atguigu.netty.learn.utils.ByteBufferUtil.debugRead;

/**
 * @author 123
 * @create 2024-08-30-22:34
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.bind(new InetSocketAddress(8888));

        List<SocketChannel> channels = new ArrayList<SocketChannel>();
        while (true) {
            log.debug("connecting...");
            //阻塞
            SocketChannel sc = ssc.accept();
            log.debug("accepted");
            channels.add(sc);
            for (SocketChannel channel : channels) {
                log.debug("reading...{}",channel);
                buffer.clear();
                //阻塞
                channel.read(buffer);
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("read complete...{}",channel);
            }
        }
    }
}
