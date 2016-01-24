package com.schremser.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by bluemax on 23.01.16.
 */
public class ChatClient {

    private final String host;
    private final int port;

    public static void main(String... args) throws Exception {
        new ChatClient("localhost", 8080).run();
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String msg = bufferedReader.readLine();
                channel.writeAndFlush(msg + "\r\n");
            }
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
