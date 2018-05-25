package com.dms.netty.chat.server;

import com.dms.netty.common.NyComMsgConst;
import com.dms.netty.server.NyServer;
import com.dms.netty.server.NyServerCodeing;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	public static void main(String[] args) {
		new NyServer().bind(NyComMsgConst.PORT);
	}
	public void bind(int port) {
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
						/**
						 * EchoServerHandler 被 标注为@Shareable，所 以我们可以总是使用 同样的实例
						 */
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//先去处理编码和解码信息
//							ch.pipeline().addLast(new NyServerCodeing());
						}
					});
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();
            System.out.println("启动完毕-----等待客户端连接*****");
            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        }catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
