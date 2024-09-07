package com.atguigu.netty.learn.rpc.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 黄锟
 * @create 2024-09-07-17:46
 */
public class UserServiceMemoryImpl implements UserService{
    private Map<String, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put("zhangsan", "123");
        allUserMap.put("lisi", "123");
        allUserMap.put("wangwu", "123");
        allUserMap.put("zhaoliu", "123");
        allUserMap.put("qianqi", "123");
    }

    /**
    * @Author: 黄锟
    * @Description: 登录
    * @DateTime: 2024/9/7 17:48
    * @Params: [username, password]
    * @Return boolean
    */
    @Override
    public boolean login(String username, String password) {
        String pass = allUserMap.get(username);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
