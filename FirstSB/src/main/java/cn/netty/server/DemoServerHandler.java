package cn.netty.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
public class DemoServerHandler extends SimpleChannelInboundHandler {
	
	//保存客户端的ChannelHandlerContext信息 根据Key（随机生成）来保存
	
	private static Map<String, ChannelHandlerContext> channels=new HashMap<>();
	
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		System.out.println("ctx/:"+ctx.channel().pipeline());
		System.out.println("现在是谁？"+ctx.channel().remoteAddress());
        System.out.println("Client say : " + msg.toString());
        //返回客户端消息 - 我已经接收到了你的消息
//        ctx.writeAndFlush("Received your message : " + msg.toString());
        //控制台输入
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

//        for (;;) {
            String line = in.readLine();
            if (line == null) {
//                //continue;
            }else{
//	            //向客户端发送数据
	            ctx.writeAndFlush(line);
            }
//        }
        
    }
	
    @Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	System.out.println("客户端已退出！"+ctx.channel().remoteAddress());
	}
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RemoteAddress : " + ctx.channel().pipeline()+ " active !");
        ctx.writeAndFlush("连接成功！");
        super.channelActive(ctx);
        System.out.println("连接成功之后，保存");
        String uuid=UUID.randomUUID().toString().replaceAll("-", "");
        channels.put(uuid, ctx);
    }
	
}
