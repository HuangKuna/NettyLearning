package com.atguigu.netty.learn.base.selector01;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.atguigu.netty.learn.utils.ByteBufferUtil.debugRead;

/**
 * @author 123
 * @create 2024-08-30-23:16
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        // 参数说明 1. 选择器，2. 监听的事件，3. 事件对应的处理对象
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // key只关注 accpt 事件，其他事件不关注
        // OP_ACCEPT 事件是服务器监听事件，客户端连接请求事件
        log.debug("reigster ssc key: {}", sscKey);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);

        ssc.socket().bind(new InetSocketAddress(8080));
        List<SocketChannel> channels = new ArrayList<SocketChannel>();
        while (true) {
//            select方法，没有事件阻塞，有事件恢复运行
            selector.select();
//            处理事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                log.debug("key: {}", key);
                //区分事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("sc: {}", sc);
                    log.debug("key: {}", scKey);
                }else if(key.isReadable()){
                    try {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        int readBytes = channel.read(buffer);
                        // 读到 -1，说明客户端正常断开连接
                        if(readBytes == -1){
                            key.cancel();
                        }else {
                            buffer.flip();
                            debugRead(buffer);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        // 异常处理
                        key.cancel();
                    }
                }
            }
        }
    }
}
