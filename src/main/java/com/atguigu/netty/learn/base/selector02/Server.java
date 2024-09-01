package com.atguigu.netty.learn.base.selector02;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.atguigu.netty.learn.utils.ByteBufferUtil.debugAll;

/**
 * @author 123
 * @create 2024-08-31-21:52
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
        log.debug("sscKey:{}", sscKey);
        ssc.bind(new InetSocketAddress(8888));
        while (true) {
            // 3. select 方法, 没有事件发生，线程阻塞，有事件，线程才会恢复运行
            // select 在事件未处理时，它不会阻塞, 事件发生后要么处理，要么取消，不能置之不理
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while(iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                log.debug("key:{}", key);
                // 5. 区分事件类型
                if (key.isAcceptable()) {// 客户端连接事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);//attachment
                    //将一个 byteBuffer 作为附件关联到 selectionKey 上
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ, buffer );
                    log.debug("sc:{}", sc);
                    log.debug("scKey:{}", scKey);
                }else if(key.isReadable()) {
                    try {
                        // 拿到触发事件的channel
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer); // 如果是正常断开，read 的方法的返回值是 -1
                        if(read == -1) {
                            key.cancel();
                        }else {
                            split(buffer);
                            // 需要扩容
                            if(buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity()* 2);
                                buffer.flip();
                                newBuffer.put(buffer); // 0123456789abcdef3333\n
                                key.attach(newBuffer);
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        key.cancel(); // 因为客户端断开了,因此需要将 key 取消（从 selector 的 keys 集合中真正删除 key）
                    }
                }
            }
        }
    }

    private static void split(ByteBuffer source) {
        source.flip();
        // 找到一条完整消息
        for (int i = 0; i < source.limit(); i++) {
            if(source.get(i) == '\n') {
                int length = i + 1 - source.position();
                // 把这条完整消息存入新的 ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从 source 读，向 target 写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact(); // 0123456789abcdef  position 16 limit 16
    }
}
