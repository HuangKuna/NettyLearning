package com.atguigu.netty.learn.rpc.protocol;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
