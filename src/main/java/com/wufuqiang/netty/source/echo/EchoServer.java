/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.wufuqiang.netty.source.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Echoes back any received data from a client.
 */
public final class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        /**
         * 1、bossGroup用于接受tcp请求，他会将请求交给workerGroup，workerGroup会获取到真正的连接
         * 然后和连接进行通信，比如读写解码编码等操作。
         * 2、EventLoopGroup是事件循环组（线程组）含有多个EventLoop，可以注册channel，用于在事件循环中去进行选择（和选择器相关）
         * 3、会创建EventExecutor数组children = new EventExecutor[nThread];
         * 每个元素的类型就是NIOEventLoop，NIOEventLoop实现了EventLoop接口和Executor接口
         *
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * ServerBootstrap对象，他是一个引导类，用于启动服务器和引导整个程序的初始化。
             * 它和ServerChannel关联，而ServerChannel继承了Channel，有一些方法remoteAddress等
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             //option方法传入 TCP参数，放在一个LinkedHashMap中
             .option(ChannelOption.SO_BACKLOG, 100)
             //handler方法传入 一个handler中，这个handler只专属于ServerSocketChannel而不是SocketChannel
             .handler(new LoggingHandler(LogLevel.INFO))
             //childHandler传入一个handler，这个handler将会在每个客户端连接 的时候调用 。供SocketChannel使用
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     if (sslCtx != null) {
                         p.addLast(sslCtx.newHandler(ch.alloc()));
                     }
                     p.addLast(new LoggingHandler(LogLevel.INFO));
                     //p.addLast(new EchoServerHandler());
                 }
             });

            // Start the server.
            // 服务器就是在这个bind方法里启动完成的
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
