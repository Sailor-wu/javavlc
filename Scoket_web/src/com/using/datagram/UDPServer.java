package com.using.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 实例化了一个DatagramSocket对象receiveSocket
 * 和一个DatagramPacket对象receivePacket，都是通过调用各自的构造函数实现的，为建立服务器做好准备。在while这个永久循环中，
 * receiveSocket这个套接字始终尝试receive()方法接收DatagramPacket数据包，当接收到数据包后，就调用
 * DatagramPacket的一些成员方法显示一些数据包的信息。在程序中调用了getAddress()获得地址，getPort()
 * 方法获得客户端套接字的端口，getData()获得客户端传输的数据。注意getData( )返回的是字节数组，我们把它转化为字符串显示。
 * 
 * @author why
 * @时间 2018年4月4日
 */
public class UDPServer {
	public static void main(String[] args) {
		try {
			//实例化了一个DatagramSocket对象receiveSocket（接收socket通道）
			DatagramSocket receiveSocket=new DatagramSocket(5000);
			//DatagramPacket对象receivePacket  （接收数据包）
			byte [] buf=new byte[102400];//缓冲区
			DatagramPacket receivePacket=new DatagramPacket(buf, buf.length);
			System.out.println("开始接收数据.......");
			//建立死循环一直接收数据，直到接收完毕发生异常
			while (true) {
				receiveSocket.receive(receivePacket);
				//获取来自那台机子的信息
				String name=receivePacket.getAddress().toString();
				//获取端口
				int port=receivePacket.getPort();
				//读取数据     重新封装数据信息，从0到最后
				String messData=new String(receivePacket.getData(),0,receivePacket.getLength());
				//组装信息
				System.out.println("来自："+name+"的机子，端口是："+port+",发送的消息是："+messData);
			}
		} catch (SocketException e) {
			System.out.println("接收Socket通道有问题"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取数据包数据异常："+e.getMessage());
			e.printStackTrace();
		}
	}
}
