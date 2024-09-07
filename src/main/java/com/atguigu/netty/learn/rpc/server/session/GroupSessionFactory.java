package com.atguigu.netty.learn.rpc.server.session;

/**
 * @author 黄锟
 * @create 2024-09-07-17:54
 */
public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}