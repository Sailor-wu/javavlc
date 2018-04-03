package org.tio.examples.showcase.client;

import org.apache.commons.lang3.StringUtils;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;
import org.tio.utils.json.Json;

import jfinal.commom.ShowcasePacket;
import jfinal.commom.intf.Tools;
import jfinal.commom.packets.GroupMsgReqBody;
import jfinal.commom.packets.JoinGroupReqBody;
import jfinal.commom.packets.LoginReqBody;
import jfinal.commom.packets.P2PReqBody;
import jfinal.commom.util.Const;
import jfinal.commom.util.Type;

/**
 *
 * @author tanyaowu
 */
public class ShowcaseClientStarter {
	static String serverIp = "127.0.0.1";
	static int serverPort = Const.PORT;

	private static Node serverNode = new Node(serverIp, serverPort);

	// 用来自动连接的，不想自动连接请设为null
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	private static ClientAioHandler aioClientHandler = new ShowcaseClientAioHandler();
	private static ClientAioListener aioListener = new ShowcaseClientAioListener();
	private static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener,
			reconnConf);

	private static AioClient aioClient = null;

	public static ClientChannelContext clientChannelContext;

	public static void command() throws Exception {
		@SuppressWarnings("resource")
		java.util.Scanner sc = new java.util.Scanner(System.in);
		int i = 1;
		StringBuilder sb = new StringBuilder();
		sb.append("使用指南:\r\n");
		sb.append(i++ + "、需要帮助，输入 '?'.\r\n");
		sb.append(i++ + "、登录，输入 'login loginname password'.\r\n");
		sb.append(i++ + "、进入群组，输入 'join group1'.\r\n");
		sb.append(i++ + "、群聊，输入 'groupMsg group1 text'.\r\n");
		sb.append(i++ + "、点对点聊天，输入 'p2pMsg loginname text'.\r\n");

		sb.append(i++ + "、退出程序，输入 'exit'.\r\n");

		System.out.println(sb);

		String line = sc.nextLine(); // 这个就是用户输入的数据
		while (true) {
			if ("exit".equalsIgnoreCase(line)) {
				System.out.println("Thanks for using! bye bye.");
				break;
			} else if ("?".equals(line)) {
				System.out.println(sb);
			}
			// 如果不是退出或者帮助，就进行操作
			processCommand(line);

			line = sc.nextLine(); // 这个就是用户输入的数据
		}

		aioClient.stop();
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {
		aioClient = new AioClient(clientGroupContext);
		clientChannelContext = aioClient.connect(serverNode);
		command();
	}

	/**
	 * 处理请求
	 * @param line
	 * @throws Exception
	 */
	public static void processCommand(String line) throws Exception {
		//判断是否为空
		if (StringUtils.isBlank(line)) {
			return;
		}
		//拆分命令
		String[] args = StringUtils.split(line, " ");
		String command = args[0];
		//登录处理
		if ("login".equalsIgnoreCase(command)) {
			String loginname = args[1];
			String password = args[2];

			LoginReqBody loginReqBody = new LoginReqBody();
			loginReqBody.setLoginname(loginname);
			loginReqBody.setPassword(password);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.LOGIN_REQ);
			System.out.println("body信息：" + Json.toJson(loginReqBody));
			reqPacket.setBody(Json.toJson(loginReqBody).getBytes(ShowcasePacket.CHARSET));

			boolean flg = Aio.send(clientChannelContext, reqPacket);
		} else if ("join".equals(command)) {//加入群处理
			String group = args[1];

			JoinGroupReqBody joinGroupReqBody = new JoinGroupReqBody();
			joinGroupReqBody.setGroup(group);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.JOIN_GROUP_REQ);
			reqPacket.setBody(Json.toJson(joinGroupReqBody).getBytes(ShowcasePacket.CHARSET));

			Aio.send(clientChannelContext, reqPacket);
		} else if ("groupMsg".equals(command)) {//群消息处理
			String group = args[1];
			String text = args[2];

			GroupMsgReqBody groupMsgReqBody = new GroupMsgReqBody();
			groupMsgReqBody.setToGroup(group);
			groupMsgReqBody.setText(text);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.GROUP_MSG_REQ);
			reqPacket.setBody(Json.toJson(groupMsgReqBody).getBytes(ShowcasePacket.CHARSET));

			Aio.send(clientChannelContext, reqPacket);
		} else if ("p2pMsg".equals(command)) {//个人对个人信息处理
			String toUserid = args[1];//拿到个人信息登录号
			String text = args[2];//信息文本
			//封装消息--点对点消息请求
			P2PReqBody p2pReqBody = new P2PReqBody();
			p2pReqBody.setToUserid(toUserid);//
			p2pReqBody.setText(text);
			//创建数据包，
			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.P2P_REQ);//设置为点对点消息类型
			reqPacket.setBody(Json.toJson(p2pReqBody).getBytes(ShowcasePacket.CHARSET));
			//发送 客户端发送完毕  服务器段处理
			Aio.send(clientChannelContext, reqPacket);
		}
	}
}
