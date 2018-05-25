package com.dms.netty.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dms.netty.common.NyCurrentChannelMap;
import com.dms.netty.common.NyMessage;
import com.dms.netty.common.NyMsgType;
import com.dms.netty.common.NySendCommand;
import com.dms.netty.util.GetClientIdByChannel;
import com.dms.netty.util.RedisUtil;
import com.dms.netty.util.SerializeUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import sun.misc.BASE64Decoder;

public class NyServerHandler extends SimpleChannelInboundHandler {
//	private Cache nettyChache;

	// private Cache nettyChache=Redis.use("rediscache");
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//		nettyChache = Redis.use("rediscache");
		JSONObject resultJson = JSON.parseObject(msg.toString());
		NyMsgType type = NyMsgType.valueOf(resultJson.getString("type"));
		String clientId = resultJson.getString("clientid");
		String data = resultJson.getString("data");
		NyMessage message = new NyMessage();
		message.setClientId(clientId);
		// 先判断有没有登录
		if (NyMsgType.LOGIN.equals(type)) {
			// 首次登录，添加到当前的map保存
			System.out.println("进来登录\t将当前的客户添加到队列\t" + clientId + "(Channel) ctx.channel():" + (Channel) ctx.channel());
			NyCurrentChannelMap.add(clientId, (Channel) ctx.channel());
			message.setType(NyMsgType.SEND);
			message.setData("{\"message\":\"success\"}");
			ctx.channel().writeAndFlush(message.toString());
		} else {
			// 不是登录，判断用户是否存在当前的map
			if (NyCurrentChannelMap.get(clientId) == null) {
				// 发送重新登录信息
				message = new NyMessage(NyMsgType.LOGIN);
				message.setClientId(clientId);
				ctx.channel().writeAndFlush(message.toString());
			} else {
				switch (type) {
				case PING:
					System.out.println("==========心跳处理==========");
					// message.setType(NyMsgType.PING);
					// ctx.channel().writeAndFlush(message.toString());
					break;
				case SEND:
					System.out.println("发送消息,给指定人clientId发。。。。");
					// NySendCommand command=new NySendCommand(clientId,
					// "你瞅啥，瞅你咋地！");
					// command.send();
					break;
				case RESPONCE:
					// 返回的消息体
					System.out.println("-----响应---" + msg);
					String messageid = resultJson.getString("messageid");
					System.out.println("messageid=" + messageid);
					// 保存
					String txtB = "";
					JSONArray jsonArray = resultJson.getJSONArray("data");
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						txtB = temp.getString("txtByte");
					}

//					GenerateTxt(messageid, txtB);
					message.setData("{\"message\":\"success\"}");
					message.setType(NyMsgType.NO_TARGET);
					ctx.channel().writeAndFlush(message.toString());
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("连接成功了*****");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		System.out.println("客户端" + GetClientIdByChannel.getClient(ctx) + "退出了！");
	}

//	private void GenerateTxt(String messageid, String txtByte) {
//		if (txtByte == null) // 字节数据为空
//			return;
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			// Base64解码
//			byte[] b = decoder.decodeBuffer(txtByte);
//			for (int i = 0; i < b.length; ++i) {
//				if (b[i] < 0) {// 调整异常数据
//					b[i] += 256;
//				}
//			}
//			// 生成txt图片
//			String realpath = PathKit.getWebRootPath();
//			String txtName = UuidUtil.getRandStr() + ".txt";
//			String txtFilePath = realpath + "/upload/";// 新生成的txt
//			File dirFile = new File(txtFilePath);
//			if (!dirFile.exists()) {
//				dirFile.mkdir();
//				System.out.println("创建目录为：" + txtFilePath);
//			}
//			OutputStream out = new FileOutputStream(txtFilePath + txtName);
//			out.write(b);
//			out.flush();
//			out.close();
//			// 根据msgid把图片路劲保存到redis
//			System.out.println("保存数据的redis:是你 " + nettyChache);
//			System.out.println("生成的messageID：" + messageid);
//			System.out.println("生成的新文件名：" + txtName);
//			nettyChache.set(messageid, txtName);// 服务器处理完毕保存
//			System.out.println("生成文件保存完毕***************************");
//			System.out.println("文件路径：" + txtFilePath);
//		} catch (Exception e) {
//			return;
//		}
//	}
}
