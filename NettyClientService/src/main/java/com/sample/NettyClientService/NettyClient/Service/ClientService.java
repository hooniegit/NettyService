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

import com.sample.NettyClientService.Class.SampleClass;
//import com.sample.NettyClientService.NettyClient.Handler.ClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        this.group = new NioEventLoopGroup();
    }
    
    public Channel getConnection() throws Exception {
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
//               p.addLast(new ClientHandler()); // Add Handler if NECESSARY
             }
         });

        ChannelFuture f = b.connect(host, port).sync();
        return f.channel();
    }

    public void sendData(Channel channel, String data) throws Exception {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(data);
        } else {
            throw new IllegalStateException("[Connection] Channel is Not Opened.");
        }
    }
    
    public void disconnect(Channel channel) throws Exception {
        if (channel != null) {
            channel.close().sync();
        }
        stop();
    } 
   
    @PreDestroy
    public void stop() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    public void start() throws Exception {
        Channel channel = getConnection();
        	
        List<SampleClass> sampleList = generateSampleList(20000);

        String message = sampleList.stream()
            .map(sample -> sample.toStream())
            .collect(Collectors.joining(",")) + "\n";
            
        sendData(channel, message);
        channel.close();
    }
    
    private SampleClass createSampleClass() {
    	SampleClass SAMPLE = new SampleClass();
    	SAMPLE.setNAME("Mike");
    	SAMPLE.setAGE(19);
    	SAMPLE.setBIRTH("2006-01-01");
    	
    	return SAMPLE;
    }
    

    private List<SampleClass> generateSampleList(int size) {
        List<SampleClass> sampleList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            SampleClass sample = createSampleClass();
            sampleList.add(sample);
        }

        return sampleList;
    }

}