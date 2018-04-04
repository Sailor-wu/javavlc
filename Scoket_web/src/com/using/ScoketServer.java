package com.using;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket服务器 注意，在选择端口时，必须小心。每一个端口提供一
 * 种特定的服务，只有给出正确的端口，才能获得相应的服务。0~1023的端口号为系统所保留，例如
 * http服务的端口号为80,telnet服务的端口号为21,ftp服务的端口号为23,
 * 所以我们在选择端口号时，最好选择一个大于1023的数以防止发生冲突。
 * @author why
 * @时间 2018年4月4日
 */
public class ScoketServer {
	public static void main(String[] args) {
		// 创建一个ServerSocket在监听8888端口请求
		ServerSocket server = null;
		try {
			server = new ServerSocket(8888);
			System.out.println("服务器端开启成功，等待链接");
		} catch (IOException e) {
			System.out.println("监听端口8888异常！" + e.getMessage());
			e.printStackTrace();
		}
		// 监听，如果有客户端请求，就创建一个Socket 并继续执行
		Socket socket = null;
		try {
			socket = server.accept();
			System.out.println("收到一个连接请求");
		} catch (IOException e) {
			System.out.println("请求过来，Server创建Socket异常" + e.getMessage());
			e.printStackTrace();
		}
		// 处理请求信息
		try {
			// 由请求对象 socket 得到输入流，并构造响应的bufferReader对象
			BufferedReader brReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 获取客户端请求信息
			System.out.println("socket客户端请求信息：" + brReader.readLine());
			// 由Socket对象得到输出流，并构造PrintWriter对象 ,保留使用（用于服务器回复的输入）
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			// 服务器回答 ,有控制台输入
			BufferedReader sysReader = new BufferedReader(new InputStreamReader(System.in));
			// 一行一行获取控制台输入的信息 直到输入bye结束
			System.out.print("开始输入：");
			String sysLine = sysReader.readLine();
			// 如果不等于bye 进来处理
			while (!sysLine.equals("bye")) {
				// 打印出来
				printWriter.println(sysLine);
				// 刷新输出流。使client马上收到该字符串
				printWriter.flush();
				System.out.println("服务器发送：" + sysLine);
				// 再接受客户端的输入
				System.out.println("客户端输入：" + brReader.readLine());
				// 写入服务器消息之后，获取客户端输入的信息
				System.out.print("开始输入：");
				sysLine = sysReader.readLine();
			}
			// 最后要关闭流
			brReader.close();
			sysReader.close();
			socket.close();
			server.close();
		} catch (IOException e) {
			System.out.println("异常信息：" + e.getMessage());
			e.printStackTrace();
		}

	}
}
