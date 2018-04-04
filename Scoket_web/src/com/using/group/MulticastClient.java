package com.using.group;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 实例化了一个MulticastSocket对象 socket，
 * 然后用join()方法加入了组播组127.0.0.1。在for循环中接收了5个数据包
 * ，并把数据包中的内容显示出来
 * 最后离开组播组（leaveGroup()），关闭套接字
 * 
 * @author why
 * @时间 2018年4月4日
 */
public class MulticastClient {
	public static void main(String[] args) {
		try {
			// 实例化MulticastSocket socket
//			MulticastSocket socket = new MulticastSocket(5555);
//			InetAddress address = InetAddress.getByName("224.0.0.1");
//			socket.joinGroup(address);
			
			InetAddress group = InetAddress.getByName("224.0.0.1");
			int port = 4446;
			MulticastSocket socket = new MulticastSocket(port);
			socket.joinGroup(group);
			DatagramPacket packet;
			for (int i = 0; i < 5; i++) {
				byte [] buf=new byte[2048];
				packet=new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				String receiveMess=new String(packet.getData());
				System.out.println("读取信息："+receiveMess);
			}
			//离开组播
			socket.leaveGroup(group);
			//关闭连接通道
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
