package com.atguigu.netty.learn.rpc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 123
 * @create 2024-09-05-23:30
 */
public abstract class Config {
    static Properties properties;
    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")){
            properties = new Properties();
            properties.load(in);
        }catch (IOException e){
            throw new ExceptionInInitializerError(e);
        }
    }
    public static int getServerPort(){
        String value = properties.getProperty("server.port");
        if (value == null){
            return 8080;
        }else {
            return Integer.parseInt(value);
        }
    }

}
