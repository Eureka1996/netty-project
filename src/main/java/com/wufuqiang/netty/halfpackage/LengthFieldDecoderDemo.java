package com.wufuqiang.netty.halfpackage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author Wu Fuqiang
 * @date 2021/10/23 10:38 下午
 */
@Slf4j
public class LengthFieldDecoderDemo {
    
    @Test
    public void lengthFieldDecoderTest1(){
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,0,4,0,4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        writeByteBuf(buffer,"hello world,hello wu");
        writeByteBuf(buffer,"wufuqiang");
        
        channel.writeAndFlush(buffer);

    } 
    
    public void writeByteBuf(ByteBuf buffer,String content){
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        final int length = bytes.length;
        buffer.writeInt(length);
        buffer.writeBytes(bytes);
    }
}
