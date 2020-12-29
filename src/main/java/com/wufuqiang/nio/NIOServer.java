package com.wufuqiang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞方式
        serverSocketChannel.configureBlocking(false);
        //创建Selector对象
        Selector selector = Selector.open();
        //把channel注册到selector，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while (true){
            if(selector.select(1000) == 0){  //没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            //如果返回的>0，就获取到相关的selectionKey集合
            //1、如果返回的>0，表示已经获取到关注的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //根据key，对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){
                    System.out.println("有新客户端连接");
                    //该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    SelectionKey register = socketChannel.register(
                            selector,
                            SelectionKey.OP_READ,
                            ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){
                    //通过key，反向获取到对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("form client:"+new String(buffer.array()));
                }

                //手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
