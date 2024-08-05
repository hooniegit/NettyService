package com.sample.NettyServerService.NettyServer.Handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.sample.NettyServerService.Class.SampleClass;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] parts = msg.split(",");

        for (String part : parts) {
        	System.out.println(part);
        	
            String[] fields = part.split(":");

            SampleClass SAMPLE = new SampleClass();
            SAMPLE.setNAME(fields[0]);
            SAMPLE.setAGE(Integer.parseInt(fields[1]));
            SAMPLE.setBIRTH(fields[2]);
            
            System.out.println("Received: " + SAMPLE);
        }

        String response = "Data received\n";
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 예외 처리
        cause.printStackTrace();
        ctx.close();
    }

    @SuppressWarnings("unchecked")
    public static List<SampleClass> deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (List<SampleClass>) objectInputStream.readObject(); // 바이트 배열을 객체 리스트로 변환
    }
    
    
}

