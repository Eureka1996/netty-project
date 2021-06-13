package com.wufuqiang.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: Wu Fuqiang
 * @create: 2021-06-11 18:16
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {

        //写出到文件
        String str = "hello world";
        FileOutputStream fileOutputStream = new FileOutputStream("");
        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(str.getBytes());
        buf.flip();
        channel.write(buf);

        fileOutputStream.close();

        //读文件
        File file = new File("");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer buf2 = ByteBuffer.allocate((int)file.length());

        fileInputStreamChannel.read(buf2);
        String str2 = new String(buf2.array());


        FileInputStream fileInputStream1 = new FileInputStream("OIP.jpeg");
        FileChannel channel1 = fileInputStream1.getChannel();

        FileOutputStream fileOutputStream1 = new FileOutputStream("OIP-copy.jpeg");
        FileChannel channel2 = fileOutputStream1.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();
            int read = channel1.read(buffer);
            if(read == -1) break;
            buffer.flip();
            channel2.write(buffer);

        }



        fileInputStream1.close();
        fileOutputStream1.close();
    }
}
