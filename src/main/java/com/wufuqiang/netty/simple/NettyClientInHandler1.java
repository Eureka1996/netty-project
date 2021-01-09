package com.wufuqiang.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientInHandler1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client handler1:" + ctx);
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("客户端handler1发往服务端：", CharsetUtil.UTF_8));
        ctx.write(Unpooled.copiedBuffer("客户端handler1发往服务端：", CharsetUtil.UTF_8));
        ctx.flush();
//        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端handler1发往服务端：", CharsetUtil.UTF_8));
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("handler1:"+buf.toString(CharsetUtil.UTF_8));
        //将数据往向一下handler传递
        ctx.fireChannelRead(Unpooled.copiedBuffer(buf.toString(CharsetUtil.UTF_8)+"-handler1",CharsetUtil.UTF_8));
    }


}
