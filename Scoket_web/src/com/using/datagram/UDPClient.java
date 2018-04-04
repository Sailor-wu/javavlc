package com.using.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * // 用DatagramSocket的构造函数实例化一个发送数据的套接字 sendSocket //
 * 实例化了一个DatagramPacket，其中数据包要发往的目的地是163.121.139.20，端口是 //
 * 5000。当构造完数据包后，就调用send( )方法将数据包发送出去。
 * 至于服务器接收的部分乱码，有待改进
 * @author why
 * @时间 2018年4月4日
 */
public class UDPClient {

	public static void main(String[] args) {
		try {
			DatagramSocket sendSocket = new DatagramSocket(3456);
			// 创建要发送的数据信息
			String sendMess = "i love you ,my love!come here...！";
			byte[] databyte = new byte[102400];
			databyte = sendMess.getBytes();
			// 实例化了一个DatagramPacket(把要发送的消息打包封装) sendPacket是要发送的数据包
			DatagramPacket sendPacket = new DatagramPacket(databyte, sendMess.length(),
					InetAddress.getByName("127.0.0.1"), 5000);
			sendSocket.send(sendPacket);
			System.out.println("send the data: 发送完毕 ! this is the client");
		} catch (SocketException e) {
			System.out.println("不能打开数据报socket,或者数据报socket无法与制定的端口连接");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("无法识别主机信息" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("网络通讯出现错误！异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}
}
