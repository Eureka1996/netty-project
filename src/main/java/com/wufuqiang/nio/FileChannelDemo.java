package com.wufuqiang.nio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author: Wu Fuqiang
 * @create: 2021-06-11 18:16
 */
public class FileChannelDemo {

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
        /**
         * ByteBuffer正确使用姿势：
         * 1、向buffer写入数据，例如调用channel.read(buffer)
         * 2、调用flip()切换到读模式
         * 3、从buffer读取数据，例如调用buffer.get()
         * 4、调用clear()或compact()切换至写模式
         * 5、重复1~4步骤
         */
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

    @Test
    public void readTxtFile2() throws IOException {
        //读文件
        File file = new File("test1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        ByteBuffer a = ByteBuffer.allocate(3);
        ByteBuffer b = ByteBuffer.allocate(3);
        ByteBuffer c = ByteBuffer.allocate(5);

        fileInputStreamChannel.read(new ByteBuffer[]{a,b,c});
        a.flip();
        b.flip();
        c.flip();
        System.out.println(StandardCharsets.UTF_8.decode(a));
        System.out.println(StandardCharsets.UTF_8.decode(b));
        System.out.println(StandardCharsets.UTF_8.decode(c));
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
