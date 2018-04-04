package com.using.group;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * MulticastServerThread类中重载了run( )方法，实现的功能基本相同，在发完服务器的信息后，用sleep(
 * )函数停止处理了一个随机的时间。
 * 
 * @author why
 * @时间 2018年4月4日
 */
public class MulticastServerThread extends QuoteServerThread {
	/// 五秒时间
	private long FIVE_SECONDS = 5000;

	public MulticastServerThread() {
		super("MulticastServerThread");
	}

	@Override
	public void run() {
		while (moreQuotes) {
			try {
				byte[] buf = new byte[2048];
				String receMess = null;
				if (reader == null) {
					receMess = new Date().toString();
				} else {
					receMess = getMessage();
				}
				buf = receMess.getBytes();
				// 发送
				InetAddress group = InetAddress.getByName("224.0.0.1");
				DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
				socket.send(packet);
				try {
					sleep((long) (Math.random() * FIVE_SECONDS));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				moreQuotes = false;
			}
		}
		socket.close();
	}

}
