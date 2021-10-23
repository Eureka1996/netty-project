package com.wufuqiang.netty.base;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author Wu Fuqiang
 * @date 2021/10/23 2:37 下午
 */
@Slf4j
public class EmbeddedChannelDemo {
    public static void main(String[] args) {
        
    }
    
    @Test
    public void embeddedChannelTest(){
        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("1 channelRead");          
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("2 channelRead");
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter h3 = new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("3 write");
                super.write(ctx, msg, promise);
            }

            @Override
            public void read(ChannelHandlerContext ctx) throws Exception {
                log.debug("3 read.");
                super.read(ctx);
            }
        };

        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("4 write");
                super.write(ctx, msg, promise);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(h1,h2,h3,h4);
        
        channel.writeInbound(
                ByteBufAllocator.DEFAULT.buffer()
                        .writeBytes("wufuqiang".getBytes(StandardCharsets.UTF_8)));

        channel.writeOutbound(
                ByteBufAllocator.DEFAULT.buffer()
                        .writeBytes("maoyujiao".getBytes(StandardCharsets.UTF_8)));
    }
}
