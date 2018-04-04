package com.using.datagram;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 这个类是查看当前端口被占用的情况 范围在1024到65535之间的端口
 * 端口号在1024以下的系统可能会用到，比如HTTP默认为80端口，FTP默认为21端口，等等，所以我们从1024端口开始探查。
 * @author why
 * @时间 2018年4月4日
 */
public class UDPScan {
	public static void main(String args[]) {
		for (int port = 1024; port <= 65535; port++) {
			try {
				DatagramSocket server = new DatagramSocket(port);
				server.close();
			} catch (SocketException e) {
				System.out.println("there is a server in port " + port + ".");
			}
		}
	}
}
