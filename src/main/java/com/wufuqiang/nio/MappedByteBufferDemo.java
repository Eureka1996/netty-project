package com.wufuqiang.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: Wu Fuqiang
 * @create: 2021-06-12 09:49
 */
public class MappedByteBufferDemo {
    public static void main(String[] args) {

    }

    public static void test1() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：使用读写模式
         * 参数2：0，起始位置
         * 参数3：5，是映射到内存的大小
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
    }
}
