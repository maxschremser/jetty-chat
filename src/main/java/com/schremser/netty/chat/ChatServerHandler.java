package com.schremser.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by bluemax on 23.01.16.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.add(ctx.channel());
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush(incoming.remoteAddress() + " has joined.\n");
            }
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush(incoming.remoteAddress() + " has left.\n");
            }
            channels.remove(ctx.channel());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("msg: " + msg);
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush(incoming.remoteAddress() + ": " + msg + "\n");
        }

    }
}
