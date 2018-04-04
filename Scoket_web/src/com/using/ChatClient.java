package com.using;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.JPanel;
@SuppressWarnings("serial")
public class ChatClient {
	private TextArea ta = new TextArea();
	private TextField tf = new TextField();
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private Socket socket = null;
	private boolean bConnected = false;
	private Thread thread = null;

	public static void main(String[] args) {
		new ChatClient().frameClient();
	}

	public void frameClient() {
//		setSize(400, 400);
		ta.setSize(400, 400);
		ta.setLocation(400, 400);
		JPanel panel=new JPanel();
		panel.add(ta,BorderLayout.NORTH);
		panel.add(tf, BorderLayout.SOUTH);
		Frame owner=new Frame();
		Window window=new Window(owner);
		window.pack();
		tf.addActionListener(new TfListener());
		// 关闭窗口事件监听
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnected();
				System.exit(0);
			}
		});
		this.connect();
		panel.setVisible(true);
	}

	// 链接服务器地址
	private void connect() {
		try {
			socket = new Socket("127.0.0.1", 8888);
			thread = new Thread(new ChatThread());
			thread.start();
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 断开连接
	private void disconnected() {
		bConnected = false;
		try {
			dos.close();
			dis.close();
			socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 键盘回车事件
	private class TfListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String strMsg = tf.getText();
			tf.setText("");
			try {
				dos.writeUTF(strMsg);
				dos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	// 开启线程接受服务器信息
	private class ChatThread implements Runnable {
		@Override
		public void run() {
			try {
				bConnected = true;
				while (bConnected) {
					String msg = dis.readUTF();
					String taText = ta.getText();
					ta.setText(taText + msg + "\n");
				}
			} catch (SocketException e) {
				System.out.println("退出");
				;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
