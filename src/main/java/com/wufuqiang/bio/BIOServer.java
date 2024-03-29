package com.wufuqiang.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
         ExecutorService executorService = Executors.newCachedThreadPool();
         //创建serversocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true){
            //监听，等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //就创建一个纯种，与之通讯
            executorService.submit(new Runnable() {
                public void run() {
                    handler(socket);
                } 
            });
        }
    }

    public static void handler(Socket socket){
        byte[] bytes = new byte[1024];
        try{
            System.out.println("线程信息 id="+Thread.currentThread().getId()+"，名称="+Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            while(true){
                int read = inputStream.read(bytes);
                if(read != -1){
                    //输出客户端发送的数据
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                System.out.println("关闭和client的连接");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

