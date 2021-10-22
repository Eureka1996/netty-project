package com.wufuqiang.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class NettyClientOutHandler1 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("out handler1:"+buf.toString(CharsetUtil.UTF_8)+"->"+System.currentTimeMillis());
        ctx.write(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"-out handler1",CharsetUtil.UTF_8));
        ctx.flush();
    }

//    @Override
//    public void read(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("read");
////        ctx.writeAndFlush("out handler1");
//    }


    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
//        ctx.pipeline().channel().
        super.read(ctx);
    }
}
