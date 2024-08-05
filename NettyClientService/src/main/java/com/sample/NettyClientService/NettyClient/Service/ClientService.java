package com.sample.NettyClientService.NettyClient.Service;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sample.NettyClientService.NettyClient.Handler.ClientHandler;

import javax.annotation.PreDestroy;

@Service
public class ClientService {

    private final String host;
    private final int port;
    private EventLoopGroup group;

    public ClientService(@Value("${netty.server.host}") String host,
                         @Value("${netty.server.port}") int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new DelimiterBasedFrameDecoder(32768000, Delimiters.lineDelimiter()));
                     p.addLast(new StringDecoder());
                     p.addLast(new StringEncoder());
                     p.addLast(new ClientHandler());
                 }
             });

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            stop();
        }
    }

    @PreDestroy
    public void stop() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }
}
