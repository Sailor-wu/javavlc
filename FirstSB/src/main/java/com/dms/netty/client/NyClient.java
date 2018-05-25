package com.dms.netty.client;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.dms.netty.common.HeartBeatClientHandler;
import com.dms.netty.common.NyComMsgConst;
import com.dms.netty.common.NyConnectionWatchdog;
import com.dms.netty.common.NyMessage;
import com.dms.netty.common.NyMsgType;
import com.dms.netty.common.ReConnectionListener;
import com.dms.netty.util.GetIPandMAC;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;

public class NyClient {
	protected final HashedWheelTimer timer = new HashedWheelTimer();

	public Channel socketChannel;

	public NyClient() {
		
		start(NyComMsgConst.SERVER, NyComMsgConst.PORT);
		
	}

	private void start(String host, int port) {
		ChannelFuture future = null;
		// 线程池
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.group(eventLoopGroup);
			bootstrap.remoteAddress(host, port);
			/*---------------------测试断线重连-------------------------*/
			final NyConnectionWatchdog watchdog = new NyConnectionWatchdog(bootstrap, timer, port, host, true) {
				public ChannelHandler[] handlers() {
					return new ChannelHandler[] { this, new IdleStateHandler(10, 10, 0, TimeUnit.SECONDS),
							new StringDecoder(), new StringEncoder(), new NyClientCodeing(),
							new HeartBeatClientHandler() };
				}
			};
			try {
				synchronized (bootstrap) {
					bootstrap.handler(new ChannelInitializer<Channel>() {
						// 初始化channel
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(watchdog.handlers());
						}
					});
					future = bootstrap.connect(host, port);
					socketChannel = future.channel();
				}
				// 以下代码在synchronized同步块外面是安全的
				future.sync();
				System.out.println("connect server 成功---------");
			} catch (Throwable t) {
				throw new Exception("connects to  fails", t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// eventLoopGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		NyClient nyClient = new NyClient();
		NyMessage message = new NyMessage(NyMsgType.LOGIN);
		String APPIDOnly = "1234567890";
		message.setClientId(APPIDOnly);
		message.setTargetId(null);
		String ip = GetIPandMAC.getYTWIp();
		String sendStr = "{\"data\":\"" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
				+ "\",\"IP\":\"" + ip + "\",\"mac\":\"" + GetIPandMAC.getMacAddress() + "\"}";
		message.setData(sendStr);
		
		nyClient.socketChannel.writeAndFlush(message.toString());

	}
}
