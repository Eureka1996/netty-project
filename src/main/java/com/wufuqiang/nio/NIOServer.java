package com.wufuqiang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
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
            // select方法，在没有事件发生，线程阻塞，在有事件发生，线程才会恢复运行
            if(selector.select(5000) == 0){  //没有事件发生
                System.out.println("服务器等待了5秒，无连接");
                continue;
            }
            //如果返回的>0，就获取到相关的selectionKey集合
            //1、如果返回的>0，表示已经获取到关注的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                // SelectionKey就是将来事件发生后，通过它可以知道事件和哪个Channel的事件。
                SelectionKey key = keyIterator.next();
                //根据key，对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){
                    System.out.println("有新客户端连接");
                    //该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);  //设置为非阻塞
                    SelectionKey register = socketChannel.register(
                            selector,
                            SelectionKey.OP_READ,
                            ByteBuffer.allocate(10));
                }else if(key.isReadable()){
                    try{
                        //通过key，反向获取到对应的channel
                        SocketChannel channel = (SocketChannel)key.channel();
                        //获取到该channel关联的buffer
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if(read == -1){
                            key.cancel();
                        }else{
                            buffer.flip();
                            String s = StandardCharsets.UTF_8.decode(buffer).toString();
                            System.out.println("utf-8:"+s);
                            //System.out.println("form client:"+new String(buffer.array()));
                            buffer.clear();
                        }
                        System.out.println("读取数据结束-----");
                    }catch (IOException e){
                        e.printStackTrace();
                        key.cancel();
                    }
                }
                //手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
