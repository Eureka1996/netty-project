package com.wufuqiang.nio;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author: Wu Fuqiang
 * @create: 2021-06-11 12:07
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for(int i = 0;i< intBuffer.capacity();i++){
            intBuffer.put(i);
        }

        //将buffer转换，读写切换
        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }


    @Test
    public void string2ByteBuffer(){
        // 字符串转ByteBuffer，方法1
        String str = "wufuqiang is a boy-----";
        ByteBuffer buffer = ByteBuffer.allocate(50);
        // 将字符串转为byte数组
        buffer.put(str.getBytes());

        //byte[] bufferArray = buffer.array();
        //for (byte buf : bufferArray) {
        //    System.out.println((char)buf);
        //}
        //System.out.println(buffer.array());
        buffer.flip();
        CharBuffer bufferToStr = StandardCharsets.UTF_8.decode(buffer);
        System.out.println(bufferToStr);

        System.out.println("------------------------");
        // 字符串转ByteBuffer，方法2
        //str = str + "maoyujiao";
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode(str);
        //System.out.println(str.length()+","+buffer2.capacity());
        //for (byte buf : buffer2.array()) {
        //    System.out.println((char) buf);
        //}

        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer2);
        System.out.println(charBuffer.toString());

        System.out.println(new String(buffer2.array()));
        System.out.println("*************************");

        ByteBuffer buffer3 = ByteBuffer.wrap(str.getBytes());
        String buffer3ToStr = StandardCharsets.UTF_8.decode(buffer3).toString();
        System.out.println(buffer3ToStr);
        //for (byte buff : buffer3.array()) {
        //    System.out.println((char)buff);
        //}
    }
}
