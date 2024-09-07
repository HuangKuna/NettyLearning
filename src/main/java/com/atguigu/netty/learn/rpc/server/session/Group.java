package com.atguigu.netty.learn.rpc.server.session;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

/**
 * @author 黄锟
 * @create 2024-09-07-17:52
 */
@Data
public class Group {
    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}

