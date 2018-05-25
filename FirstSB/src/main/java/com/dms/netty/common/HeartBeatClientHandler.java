package com.dms.netty.common;

import java.util.Date;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("重新激活时间是：" + new Date());
		System.out.println("HeartBeatClientHandler channelActive");
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("服务器停止时间是：" + new Date());
		System.out.println("HeartBeatClientHandler channelInactive");
	}
}
