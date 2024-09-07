# Netty学习

## Netty入门

### 1.概述
####  1.1 Nettys是什么
```
Netty is an asynchronous event-driven network application framework
for rapid development of maintainable high performance protocol servers & clients.
```
Netty是一个异步的事件驱动的网络应用框架，用于快速开发可维护的高性能协议服务器和客户端。

#### 1.2 Netty 的作者
![](img/0005.png)

他还是另一个著名网络应用框架 Mina 的重要贡献者

#### 1.3 Netty 的地位
Netty 在 Java 网络应用框架中的地位就好比：Spring 框架在 JavaEE 开发中的地位

以下的框架都使用了 Netty，因为它们有网络通信需求！

* Cassandra - nosql 数据库
* Spark - 大数据分布式计算框架
* Hadoop - 大数据分布式存储框架
* RocketMQ - ali 开源的消息队列
* ElasticSearch - 搜索引擎
* gRPC - rpc 框架
* Dubbo - rpc 框架
* Spring 5.x - flux api 完全抛弃了 tomcat ，使用 netty 作为服务器端
* Zookeeper - 分布式协调框架

#### 1.4 Netty 的优势

* Netty vs NIO，工作量大，bug 多
    * 需要自己构建协议
    * 解决 TCP 传输问题，如粘包、半包
    * epoll 空轮询导致 CPU 100%
    * 对 API 进行增强，使之更易用，如 FastThreadLocal => ThreadLocal，ByteBuf => ByteBuffer
* Netty vs 其它网络应用框架
    * Mina 由 apache 维护，将来 3.x 版本可能会有较大重构，破坏 API 向下兼容性，Netty 的开发迭代更迅速，API 更简洁、文档更优秀
    * 久经考验，16年，Netty 版本
        * 2.x 2004
        * 3.x 2008
        * 4.x 2013
        * 5.x 已废弃（没有明显的性能提升，维护成本高）

### 2. Hello World

#### 2.1 目标

开发一个简单的服务器端和客户端

* 客户端向服务器端发送 hello, world
* 服务器仅接收，不返回

加入依赖
```xml
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.39.Final</version>
</dependency>
```
#### 2.2 服务器端

```java
public static void main(String[] args) {
    new ServerBootstrap()
            .group(new NioEventLoopGroup()) // 1
            .channel(NioServerSocketChannel.class) // 2
            .childHandler(new ChannelInitializer<NioSocketChannel>() { // 3
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new StringDecoder()); // 5
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() { // 6
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                            System.out.println(msg);
                        }
                    });
                }
            })
            .bind(8080); // 4
}
```
代码解读

* 1 处，创建 NioEventLoopGroup，可以简单理解为 `线程池 + Selector` 后面会详细展开

* 2 处，选择服务 Scoket 实现类，其中 NioServerSocketChannel 表示基于 NIO 的服务器端实现，其它实现还有

  ![](img/0006.png)

* 3 处，为啥方法叫 childHandler，是接下来添加的处理器都是给 SocketChannel 用的，而不是给 ServerSocketChannel。ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel 以便添加更多的处理器

* 4 处，ServerSocketChannel 绑定的监听端口

* 5 处，SocketChannel 的处理器，解码 ByteBuf => String

* 6 处，SocketChannel 的业务处理器，使用上一个处理器的处理结果

