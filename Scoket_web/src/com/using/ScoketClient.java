package com.using;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * @author why
 * @时间 2018年4月4日
 */
public class ScoketClient {
	public static void main(String[] a) {
		try {
			//向本机的8888端口发出请求
			Socket socket=new Socket("127.0.0.1", 8888);
			System.out.println("连接主机成功*****");
			System.out.println("你可以发送消息了");
			//发送请求，有控制台输入请求信息
			BufferedReader cliReader=new BufferedReader(new InputStreamReader(System.in));
			//输出控制台显示  通过socket对象得到输出流，构造打印输出对象
			PrintWriter writer=new PrintWriter(socket.getOutputStream());
			//由socket对象获得输入流，并且构造相对应的输入对象、
			BufferedReader cliInReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//获取输入的信息  直到输入bye为整个socket通讯结束。（就好像打电话，说拜拜就拜拜了）
			System.out.print("开始输入：");
			String  readerLine=cliReader.readLine();
			while (!readerLine.equals("bye")) {
				//写入输入的信息到数据流
				writer.println(readerLine);
				//发送server
				writer.flush();
				System.out.println("client say:"+readerLine);
				//读取服务器回复信息
				System.out.println("server say:"+cliInReader.readLine());
				//客户端继续输入
				System.out.print("开始输入：");
				readerLine=cliReader.readLine();
			}
			writer.close();
			cliInReader.close();
			socket.close();
		} catch (UnknownHostException e) {
			System.out.println("主机解析错误!"+e.getMessage());
//			e.printStackTrace();	
		} catch (IOException e) {
			System.out.println("数据流异常："+e.getMessage());
//			e.printStackTrace();
		}
		
	}
}
