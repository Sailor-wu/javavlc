package com.using.group;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

/**
 * 在QuoteServerThread类的run()函数中，服务器端套接字接收来自客户端的数据包，并从文件中读取数据，把信息发给客户端。
 * 
 * @author why
 * @时间 2018年4月4日
 * 
 */
public class QuoteServerThread extends Thread {

	DatagramSocket socket = null;
	BufferedReader reader = null;
	boolean moreQuotes = true;

	public QuoteServerThread() {
		this("QuoteServerThread");
	}

	public QuoteServerThread(String name) {
		super(name);
		// 初始化socket通道
		try {
			socket = new DatagramSocket(8888);
			// 创建要发送组播的消息
			reader = new BufferedReader(new FileReader("D:\\eclipseWorkspace\\Scoket_web\\src\\one-liners.txt"));
		} catch (SocketException e) {
			System.out.println("socket通道异常！");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在异常：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (moreQuotes) {
			try {
				// 每次以bs的大小读取文件，也就是2048B=2kB,
				byte[] bs = new byte[2048];
				// 获取请求
				DatagramPacket packet = new DatagramPacket(bs, bs.length);
				socket.receive(packet);
				// 进行响应
				String receMess = null;
				if (reader == null) {
					receMess = new Date().toString();// 返回当前系统时间
				} else {
					// 获取数据
					receMess = getMessage();
				}
				bs = receMess.getBytes();
				// 向用户发送
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(bs, bs.length, address, port);
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
				moreQuotes = false;// 发生异常，读取完毕
			}
		}
		socket.close();
	}

	protected String getMessage() {
		String returnStr = null;
		try {
			if ((returnStr = reader.readLine()) == null) {
				reader.close();
				moreQuotes = false;
				returnStr = "No more quotes. Goodbye.";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnStr;
	}

}
