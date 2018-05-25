package com.dms.netty.common;

import java.util.Date;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.Channel;
public class NySendCommand {

	private String clientid;
	private String sendMsg;
	private Long messid;
	private NyMessage message = new NyMessage();

	public NySendCommand(String clientid, String sendMsg) {
		this.clientid = clientid;
		this.sendMsg = sendMsg;
	}

	public String send() {
		// RedisUtil.getCaChe().set("12344444", "abcdefghijklmnopqrstu");
		// System.out.println("12344444"+RedisUtil.getCaChe().get("12344444").toString());

		message.setData(sendMsg);
		message.setClientId(clientid);
		message.setType(NyMsgType.REQUEST);
		System.out.println("clientid:" + clientid);

		Channel channel = NyCurrentChannelMap.get(clientid);
		System.out.println("request：" + channel);

		JSONObject resultJson = JSON.parseObject(message.toString());
		messid = getMsgId();
		resultJson.put("messageid", messid);// 指定消息的id

		channel.writeAndFlush(resultJson.toString());
		
		String responceStr = GetTxt(messid);
		System.out.println("返回的新文件：" + responceStr);
		return responceStr;
	}

	private String GetTxt(Long messid) {
		Date sendStartTime = new Date();
		String responStr = "";
		for (int i = 0; i < 3; i++) {
			// 接受Tcp消息的时间
			Date acceptDataTime = new Date();
			// 获取数据的时间间隔
			long getInterval = (acceptDataTime.getTime() - sendStartTime.getTime()) / 1000;
			// 从Redis数据库中获取值
			//responStr = nettyChache.get(messid);
			System.out.println("获取msgid***************" + messid);
			if (getInterval >= 2500L) {// 如果超过等待时间则立即跳出
				break;
			} else if (responStr != null && responStr != "") {
				break;
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return responStr;
	}

	// 生成无边界的Int,用来做序列号id
	private int random_IntUnbounded() {
		int intUnbounded = new Random().nextInt();
		if (intUnbounded < 0) {
			intUnbounded = -intUnbounded;
		}
		return intUnbounded;
	}

	// 生成消息唯一id MsgId ，通过 orgcode 的hashCode + 时间戳 + 随机数字
	private Long getMsgId() {
		Long curTime = System.currentTimeMillis();
		Long authAppIdHash = (long) clientid.hashCode();
		int randomInt = random_IntUnbounded();
		return Long.valueOf(curTime + authAppIdHash + randomInt);
	}
}
