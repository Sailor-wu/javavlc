package com.dms.netty.common;

import io.netty.bootstrap.Bootstrap;  
import io.netty.channel.Channel;  
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelFutureListener;  
import io.netty.channel.ChannelHandler.Sharable;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter;  
import io.netty.channel.ChannelInitializer;  
import io.netty.util.Timeout;  
import io.netty.util.Timer;  
import io.netty.util.TimerTask;  
  
import java.util.concurrent.TimeUnit;  
  
/** 
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行重连 
 */  
@Sharable  
public abstract class NyConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask ,NyChannelHandlerHolder{  
    private  Bootstrap bootstrap;  
    private  Timer timer;  
    private  int port;  
    private  String host;  
    private volatile boolean reconnect = true;  
    private int attempts;  
      
    public NyConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port,String host, boolean reconnect) {  
        this.bootstrap = bootstrap;  
        this.timer = timer;  
        this.port = port;  
        this.host = host;  
        this.reconnect = reconnect;  
    }  
    /** 
     * channel链路每次active的时候，将其连接的次数重新归    0 
     */  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");  
        attempts = 0;  
        ctx.fireChannelActive();  
    }  
    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("链接关闭");  
        if(reconnect){  
            System.out.println("链接关闭，将进行重连");  
            if (attempts < 12) {  
                attempts++;  
                //重连的间隔时间会越来越长 /**
                /**
                 * 2<<1 =1    
                 * 2<<2 =2  
                 * 2<<3=4  
                 * 2<<4=8 
                 * 2<<5=16
                 */
                int timeout = 2 << attempts;  
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS); //单位秒 
            }  
        }  
        ctx.fireChannelInactive();  
    }  
      
  
    public void run(Timeout timeout) throws Exception {  
        ChannelFuture future;  
        //bootstrap已经初始化好了，只需要将handler填入就可以了  
        synchronized (bootstrap) {  
            bootstrap.handler(new ChannelInitializer<Channel>() {  
                @Override  
                protected void initChannel(Channel ch) throws Exception {  
                    ch.pipeline().addLast(handlers());  
                }  
            });  
            future = bootstrap.connect(host,port);  
        }  
        //future对象  
        future.addListener(new ChannelFutureListener() {  
            public void operationComplete(ChannelFuture f) throws Exception {  
                boolean succeed = f.isSuccess();  
                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连  
                if (!succeed) {  
                    System.out.println("重连失败");  
                    f.channel().pipeline().fireChannelInactive();  
                }else{  
                    System.out.println("重连成功");  
                }  
            }  
        });  
          
    }  
  
} 
