package com.atguigu.netty.learn.rpc.server.service;

/**
 * @author 123
 * @create 2024-09-07-17:23
 */
public interface UserService {

    /**
    * @Author: 黄锟
    * @Description:
    * @DateTime: 2024/9/7 17:41
    * @Params: [username , password]
    * @Return boolean
    */
    boolean login(String username, String password);

}
