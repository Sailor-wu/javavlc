package com.dms.netty.chat.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.dms.netty.client.NyClientCodeing;
import com.dms.netty.common.NyComMsgConst;
import com.dms.netty.common.NyMessage;
import com.dms.netty.common.NyMsgType;
import com.dms.netty.util.GetIPandMAC;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {
	public static void main(String[] args) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(//socketChannel.pipeline().addLast(new IdleStateHandler(20,10,0));
					//new NyClientCodeing()
					new ChannelInitializer<SocketChannel>(){
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IdleStateHandler(20,10,0));
							ch.pipeline().addLast(new NyClientCodeing());
						}}
					);
			// 连接客户端
			Channel channel = b.connect(NyComMsgConst.SERVER, NyComMsgConst.PORT).sync().channel();
			for (;;) {
//				int max = 20000;
//				int min = 10;
//				Random random = new Random();
//				int curStr = random.nextInt(max) % (max - min + 1) + min;
//				int curSt2 = random.nextInt(max) % (max - min + 1) + min;
//				String appId = UuidUtil.getRandStr().substring(0, 18);
//				String ip = GetIPandMAC.getYTWIp();
//				String APPIDOnly = "82c1weqwe123";
//				String sendStr = "{\"cmd_type\":\"tcp_connect\",\"authorizer_appid\":\"" + APPIDOnly
//						+ "\",\"orgCode\":\"0000\",\"MsgId\":\"" + curStr + curSt2 + "\",\"data\":\""
//						+ new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\",\"IP\":\"" + ip + "\",\"mac\":\""
//						+ GetIPandMAC.getMacAddress() + "\"}";
//				
//				NyMessage message=new NyMessage(NyMsgType.LOGIN);
//				message.setClientId(APPIDOnly);
//				message.setTargetId(null);
//				message.setData(sendStr);
//				channel.writeAndFlush(message.toString());
				
//				channel.writeAndFlush(sendStr);
			}
		} finally {
			// 优雅退出，释放线程池资源
			group.shutdownGracefully();
		}

	}
}
