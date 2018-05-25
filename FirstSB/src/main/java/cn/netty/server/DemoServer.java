package cn.netty.server;
import java.io.IOException;

import cn.netty.common.ComMsgConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器启动逻辑
 */
public class DemoServer {
    public static void main(String[] args) throws Exception {
        //int port = 8000;
//        if (args != null && args.length > 0) {
//            try {
//                port = Integer.valueOf(args[0]);
//            } catch (NumberFormatException e) {
//                //采用默认值
//            }
//        }
        new DemoServer().bind(ComMsgConst.PORT);
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
							ch.pipeline().addLast(new ServerChannelInitializer());
						}
					});
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

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
