package com.wufuqiang.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientInHandler2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client handler2:" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端handler2发往服务端：", CharsetUtil.UTF_8));
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("inbound handler2 channelRead:"+buf.toString(CharsetUtil.UTF_8));
        Thread.sleep(10000);
        ctx.fireChannelRead(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"-> inbound handler1",CharsetUtil.UTF_8));
//        ctx.writeAndFlush(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"-handler2",CharsetUtil.UTF_8));
//        System.out.println("write");
        //ctx.channel().writeAndFlush(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"-handler2",CharsetUtil.UTF_8));
//        ctx.write(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"-handler2",CharsetUtil.UTF_8));
//        ctx.flush();
//
//        ctx.writeAndFlush(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"in1",CharsetUtil.UTF_8));
//        ctx.writeAndFlush(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"in2",CharsetUtil.UTF_8));
    }
}
