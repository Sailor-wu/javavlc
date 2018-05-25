package com.dms.netty.util;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Enumeration;

//import com.jfinal.kit.PathKit;

/**
 * 获取IP和MAC
 * 
 * @author Administrator
 */
public class GetIPandMAC {

	/**
	 * 获取本机的IPv4 IP
	 * 
	 * @return
	 */
	public static String getYTWIp() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 获取本机所有的IP
	 * 
	 * @return
	 */
	public static InetAddress getIp() {
		InetAddress ip = null;
		try {
			Enumeration<NetworkInterface> interfaces = null;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = interfaces.nextElement();
				Enumeration<InetAddress> addresss = ni.getInetAddresses();
				while (addresss.hasMoreElements()) {
					InetAddress nextElement = addresss.nextElement();
					String hostAddress = nextElement.getHostAddress();
					System.out.println("本机IP地址为：" + hostAddress);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
	/**
	 * 获取MAC
	 * @param ip
	 * @return
	 */
	public static String getMACByIp(InetAddress ip) {
		NetworkInterface network;
		StringBuilder sb = new StringBuilder();
		try {
			network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			System.out.print("Current MAC address : ");
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			System.out.println(sb.toString());
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * 获取IPv4 MAC
	 * @return
	 */
	public static String getMacAddress() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			byte[] mac = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					mac = netInterface.getHardwareAddress();
					if (mac != null) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < mac.length; i++) {
							sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
						}
						if (sb.length() > 0) {
							return sb.toString();
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
//		getIp();
//		System.out.println("以太网IP：" + getYTWIp());
//		System.out.println("物理地址："+getMacAddress());
		// getMACByIp(getYTWIp());
		int num=(int) (Math.random()*7);
		System.out.println(num);
		
	}
}
