package com.dms.netty.server;

import com.dms.netty.common.NyComMsgConst;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NyServer {

	public static void main(String[] args) {
		new NyServer().bind(NyComMsgConst.PORT);
	}

	// 配置服务端的NIO线程组
	public  static EventLoopGroup bossGroup = new NioEventLoopGroup();
	public static EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void bind(int port) {
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						/**
						 * EchoServerHandler 被 标注为@Shareable，所 以我们可以总是使用 同样的实例
						 */
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// 先去处理编码和解码信息
							ch.pipeline().addLast(new NyServerCodeing());
						}
					});
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			System.out.println("启动完毕-----等待客户端连接*****");
			// 等待服务器监听端口关闭
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * System.err.println("nettyListener Startup!");  
        new Thread(){  
            @Override  
            public  void run(){  
                try {  
                	new NyServer().bind(NyComMsgConst.PORT);
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }.start();  
        System.err.println("nettyListener end!");
	 */

	/**
	 * 启动插件，有异常就返回false
	 */
	/*@Override
	public boolean start() {
		System.err.println("netty Startup!");  
        new Thread(){  
            @Override  
            public  void run(){  
                try {  
                	new NyServer().bind(NyComMsgConst.PORT);
                } catch (Exception e) {  
                    e.printStackTrace(); 
                }  
            }  
        }.start();  
        System.err.println("netty end!");
		return true;
	}

	@Override
	public boolean stop() {
		try {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		} catch (Exception e) {
			return false;
		}
		return true;
	}*/
}
