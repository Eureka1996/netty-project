package com.wufuqiang.nio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: Wu Fuqiang
 * @create: 2021-06-11 18:16
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {

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

    /**
     * 利用FileChannel将String写入文件中
     * @throws IOException
     */
    @Test
    public void writeFile() throws IOException {
        //写出到文件
        String str = "hello world,hello wufuqiang";
        FileOutputStream fileOutputStream = new FileOutputStream("test.txt");
        // 获取输出流渠道
        FileChannel channel = fileOutputStream.getChannel();
        // 定义Buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(str.getBytes());
        buf.flip();
        channel.write(buf);

        fileOutputStream.close();
    }

    /**
     * 利用FileChannel读取文本文件
     * @throws IOException
     */
    @Test
    public void readTxtFile() throws IOException {
        //读文件
        File file = new File("test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer buf2 = ByteBuffer.allocate((int)file.length());

        fileInputStreamChannel.read(buf2);
        String str2 = new String(buf2.array());
        System.out.println(str2);
    }

    /**
     * 利用FileChannel复制图片
     * @throws IOException
     */
    @Test
    public void copyPicture() throws IOException {
        // 原图片
        FileInputStream fileInputStream1 = new FileInputStream("OIP.jpeg");
        FileChannel channel1 = fileInputStream1.getChannel();
        // 复制后的图片
        FileOutputStream fileOutputStream1 = new FileOutputStream("OIP-copy.jpeg");
        FileChannel channel2 = fileOutputStream1.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            // 清除缓存中原有的数据
            buffer.clear();
            // 将数据写入buffer
            int read = channel1.read(buffer);
            // 已经结束，可以跳出
            if(read == -1) break;
            // 将buffer的指针重置
            buffer.flip();
            channel2.write(buffer);
        }

        fileInputStream1.close();
        fileOutputStream1.close();
    }
}
