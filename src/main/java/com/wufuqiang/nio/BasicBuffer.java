package com.wufuqiang.nio;

import java.nio.IntBuffer;

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
}
