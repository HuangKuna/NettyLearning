package com.atguigu.netty.learn.rpc.server.session;

/**
 * @author 黄锟
 * @create 2024-09-07-17:55
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
