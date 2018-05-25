package com.dms.netty.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.netty.common.NyComMsgConst;
import com.dms.netty.common.NyCurrentChannelMap;
import com.dms.netty.common.NyMessage;
import com.dms.netty.common.NyMsgType;
import com.dms.netty.server.NyServer;
import com.dms.netty.util.ByteUtil;
import com.dms.netty.util.GetClientIdByChannel;
import com.dms.netty.util.GetIPandMAC;
import com.dms.netty.util.RedisUtil;
import com.dms.netty.util.SerializeUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class NyClientHandler extends SimpleChannelInboundHandler {
	
	private String clientId;
	private NyMsgType type;
	private NyMessage message;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		JSONObject resultJson = JSON.parseObject(msg.toString());
		clientId = resultJson.getString("clientid");//

		type = NyMsgType.valueOf(resultJson.getString("type"));
		message = new NyMessage();
		message.setClientId(clientId);
		switch (type) {
		case SEND:
			System.out.println("登錄成功！！！");
			break;
		case LOGIN:
			
			message = new NyMessage(NyMsgType.LOGIN);
			String APPIDOnly = "1234567890";
			message.setClientId(APPIDOnly);
			message.setTargetId(null);
			String ip = GetIPandMAC.getYTWIp();
			String sendStr = "{\"data\":\"" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
					+ "\",\"IP\":\"" + ip + "\",\"mac\":\"" + GetIPandMAC.getMacAddress() + "\"}";
			message.setData(sendStr);
			ctx.writeAndFlush(message.toString());
			
			break;
		case REQUEST:
			System.out.println("request:" + msg);
			int num = (int) (Math.random() * 7);
			String txt = ByteUtil.Base64CgByteToString(ByteUtil.image2byte("d:/txt/" + num + ".txt"));
			String messageid = resultJson.getString("messageid");
			message.setType(NyMsgType.RESPONCE);
			message.setData("{\"txtByte\":\"" + txt + "\"}");
			JSONObject result = JSON.parseObject(message.toString());
			result.put("messageid", messageid);
			ctx.writeAndFlush(result.toString());
			break;
		default:
			break;
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case WRITER_IDLE:
				NyMessage pingMsg = new NyMessage(NyMsgType.PING);
				pingMsg.setClientId(clientId);
				pingMsg.setData("{\"message\":\"心跳包\"}");
				System.out.println("-----------------------发送心跳包数据--------------------------");
				ctx.writeAndFlush(pingMsg.toString());
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("***************服务器异常，准备重连***************");
	}
}
