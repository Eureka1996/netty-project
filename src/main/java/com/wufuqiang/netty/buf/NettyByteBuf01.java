package com.wufuqiang.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class NettyByteBuf01 {
    public static void main(String[] args) {
        
        //创建一个ByteBuf
        //说明
        //1. 创建对象，该对象包含一个数组arr , 是一个byte[10]
        //2. 在netty 的buffer中，不需要使用flip 进行反转
        //   底层维护了 readerindex 和 writerIndex
        //3. 通过 readerindex 和  writerIndex 和  capacity， 将buffer分成三个区域
        // 0---readerindex 已经读取的区域
        // readerindex---writerIndex ， 可读的区域
        // writerIndex -- capacity, 可写的区域
        ByteBuf buffer = Unpooled.buffer(10);
        log.debug("ByteBuffer:"+buffer);
        for(int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity=" + buffer.capacity());//10
        //输出
//        for(int i = 0; i<buffer.capacity(); i++) {
//            System.out.println(buffer.getByte(i));
//        }
        for(int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
        System.out.println("执行完毕");
    }
    
    @Test
    public void byteBufferPool(){
        final ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        log.debug("buf1--->{}",buf1);

        final ByteBuf buf2 = ByteBufAllocator.DEFAULT.heapBuffer();
        log.debug("buf2--->{}",buf2);

        final ByteBuf buf3 = Unpooled.buffer();
        log.debug("buf3--->{}",buf3);
    }
    
    @Test
    public void byteBufSlice(){
        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});

        final ByteBuf slice1 = buf.slice(0, 5);
        final ByteBuf slice2 = buf.slice(5, 5);

        slice1.setByte(0,'P');
        
        log.debug("buf={}",buf.toString(CharsetUtil.UTF_8));
        final String slice1Str = slice1.toString(CharsetUtil.UTF_8);
        log.debug("slice1Str={}",slice1Str);

        log.debug("slice2Str={}",slice2.toString(CharsetUtil.UTF_8));


    }
}
