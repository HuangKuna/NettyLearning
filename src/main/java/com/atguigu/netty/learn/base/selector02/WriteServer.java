package com.atguigu.netty.learn.base.selector02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author 123
 * @create 2024-08-31-22:35
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8888));

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        
        while (true) {
            selector.select();

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    //也可以 ssc.accept();
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector,0,null);
                    //向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 3000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    int write = sc.write(buffer);
                    System.out.println(write);
                    if(buffer.hasRemaining()) {
                        sckey.interestOps(sckey.interestOps()+SelectionKey.OP_WRITE);
                        sckey.attach(buffer);
                    }
                }else if(key.isReadable()){
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    SocketChannel sc =(SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    if(!buffer.hasRemaining()) {
                        key.attach(null);//清空附件
                        key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);//取消写事件
                    }
                }
            }
        }
    }
}
