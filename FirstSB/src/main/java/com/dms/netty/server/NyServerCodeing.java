package com.dms.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NyServerCodeing extends ChannelInitializer<SocketChannel> {

	/**
	 * 设置管道水管的编码和解码
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		// 字符串解码和编码
		pipeline.addLast("decoder", new StringDecoder());

		pipeline.addLast("encoder", new StringEncoder());
		// 服务器的逻辑
		pipeline.addLast("handler", new NyServerHandler());
	}
}
