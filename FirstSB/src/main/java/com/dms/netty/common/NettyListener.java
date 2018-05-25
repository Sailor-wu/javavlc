package com.dms.netty.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dms.netty.server.NyServer;

public class NettyListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		NyServer.bossGroup.shutdownGracefully();
		NyServer.workerGroup.shutdownGracefully();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.err.println("nettyListener Startup!");  
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
       
		
	}

}
