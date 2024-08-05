package com.sample.NettyClientService.NettyClient.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sample.NettyClientService.Class.SampleClass;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        List<SampleClass> sampleList = generateSampleList(20000);

        String message = sampleList.stream()
            .map(sample -> sample.toStream())
            .collect(Collectors.joining(",")) + "\n";

        ctx.writeAndFlush(message);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
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

