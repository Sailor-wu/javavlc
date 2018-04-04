package com.using;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	Socket socket = null; // 保存与本线程相关的Socket对象
	int clientnum; // 保存本进程的客户计数

	public ServerThread(Socket socket, int num) { // 构造函数
		this.socket = socket; // 初始化socket变量
		clientnum = num + 1; // 初始化clientnum变量
	}

	@Override
	public void run() {
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
		} catch (IOException e) {
			System.out.println("异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}

}
