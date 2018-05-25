package com.dms.netty.common;

import java.util.concurrent.TimeUnit;

import com.dms.netty.client.NyClient;
import com.dms.netty.server.NyServer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

public class ReConnectionListener implements ChannelFutureListener {


	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		if (!channelFuture.isSuccess()) {
			final EventLoop loop = channelFuture.channel().eventLoop();
			loop.schedule(new Runnable() {
				@Override
				public void run() {
					System.err.println("服务端链接不上，开始重连操作...");
					 new NyClient();
				}
			}, 1L, TimeUnit.SECONDS);
		} else {
			System.err.println("服务端链接成功...");
		}
	}

}
