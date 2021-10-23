package com.wufuqiang.netty.halfpackage;

import com.wufuqiang.netty.http.HttpServerInitializerExample;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HalfPackageServer {
    public static void main(String[] args) throws Exception {
        //1 创建bossGroup的Nio事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //2 创建workerGroup的Nio事件循环组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //3 创建服务端引导程序
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 调整操作系统的接收缓冲区(滑动窗口)
            serverBootstrap.option(ChannelOption.SO_RCVBUF,7);
            // 调整netty的接收缓冲区(ByteBuf)
            serverBootstrap.childOption(
                    ChannelOption.RCVBUF_ALLOCATOR,
                    new AdaptiveRecvByteBufAllocator(7,7,7));
            //4 为引导程序指定事件组，包括bossGroup和workerGroup
            serverBootstrap.group(bossGroup, workerGroup)
                    //5 指定服务器通道
                    .channel(NioServerSocketChannel.class)
                    //6 设置一些配置参数
                    //7 指定handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        }
                    });

            //8 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(6669).sync();
            //9 添加监听器
//            channelFuture.addListener()
            System.out.println("wufuqiang");
            //10 关闭端口
            channelFuture.channel().closeFuture().sync();
            System.out.println("wufuqiang");
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
