package jfinal.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tio.client.ClientChannelContext;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.server.ServerGroupContext;
import org.tio.utils.json.Json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

import jfinal.commom.RedisUtil;
import jfinal.commom.ShowcasePacket;
import jfinal.commom.intf.Tools;
import jfinal.commom.packets.P2PReqBody;
import jfinal.commom.packets.P2PRespBody;
import jfinal.commom.util.Type;
import jfinal.tio.server.ShowcaseServerStarter;

public class HelloController extends Controller {

	public void index() {
	}

	/**
	 * 多个 sendToAll 获取serverGroupContext 所有的用户组
	 */
	public void group() throws UnsupportedEncodingException {
		String type = getPara("all");
		String mess = getPara("text");
		ShowcasePacket packet = new ShowcasePacket();
		packet.setType(Type.GROUP_MSG_RESP);
		packet.setBody(Json.toJson(mess).getBytes(ShowcasePacket.CHARSET));
		ServerGroupContext group = ShowcaseServerStarter.serverGroupContext;
		Aio.sendToAll(group, packet);
	}

	/**
	 * 单个 user token
	 * 
	 * @throws InterruptedException
	 */
	public void One() throws UnsupportedEncodingException, InterruptedException {
		String userId = getPara("u");
		String mess = getPara("m");
		String messId = Tools.getUuId();
		// String messgeBody="{'mess':''随机Id',''mess':mess}";
		// 发送消息成功 等待10秒 读取缓存中客户端是否有返回
		if (this.sendMessage(userId, mess, messId)) {
			// 等待10 ，读取缓存数据
			Thread.sleep(13000);
			// System.out.println("消息："+);
			// String respMess=RedisUtil.getKey("随机Id").toString();
			// if(null!=respMess||"".equals(respMess)){
			renderJson("{\"successMsg\":\"用户忙.........\"}");
			// }
			// renderJson("{\"successMsg\":\""+respMess+"\"}");
		} else {
			// 返回超时
			renderJson("{\"successMsg\":\"error\"}");
		}
		// RedisUtil.jedis.del("随机Id");
		// ShowcasePacket packet=new ShowcasePacket();
		// packet.setType(Type.P2P_RESP);
		// packet.setBody(Json.toJson(mess).getBytes(ShowcasePacket.CHARSET));
		// ServerGroupContext group=ShowcaseServerStarter.serverGroupContext;
		// Aio.sendToUser(group, userId, packet);
		// -------
		// ShowcasePacket respPacket = new ShowcasePacket();
		// P2PRespBody respBody =new P2PRespBody();
		// respBody.setFromUserid(TestRedis.SERVERID);
		// respBody.setText(mess);
		// respPacket.setType(Type.P2P_RESP);
		// respPacket.setBody(Json.toJson(respBody).getBytes(ShowcasePacket.CHARSET));
		// ServerGroupContext
		// channelContext=ShowcaseServerStarter.serverGroupContext;
		// Aio.sendToUser(channelContext, userId, respPacket);
		// Thread.sleep(15000);
		// System.out.println("消息："+TestRedis.getKey("server"));
		// renderJson("{\"successMsg\":\"success\"}");
	}

	public void One2() throws UnsupportedEncodingException, InterruptedException {
		String userId = getPara("u");
		String mess = getPara("m");
		// String messgeBody="{'mess':''随机Id',''mess':mess}";
		// 发送消息成功 等待10秒 读取缓存中客户端是否有返回
		// if (this.sendMessage(userId, mess)) {
		// // 等待10 ，读取缓存数据
		// Thread.sleep(13000);
		// // System.out.println("消息："+);
		// // String respMess=RedisUtil.getKey("随机Id").toString();
		// // if(null!=respMess||"".equals(respMess)){
		// renderJson("{\"successMsg\":\"用户忙.........\"}");
		// // }
		// // renderJson("{\"successMsg\":\""+respMess+"\"}");
		// } else {
		// // 返回超时
		// renderJson("{\"successMsg\":\"error\"}");
		// }
		
		String respMess = sendMessage(userId, mess);
		renderJson("{\"successMsg\":\"" + respMess + "\"}");
	}

	private String sendMessage(String userId, String mess) throws UnsupportedEncodingException, InterruptedException {
		String messId = Tools.getUuId();
		ServerGroupContext groupContext = ShowcaseServerStarter.serverGroupContext;
		// ClientChannelContext
		// clientChannelContext=ShowcaseServerStarter.clientChannelContext;
		System.out.println("发送消息。。。。。。");
		// 创建响应实体 分别设置消息类型，消息内容
		P2PReqBody p2pReqBody = new P2PReqBody();

		p2pReqBody.setToUserid(userId);//
		p2pReqBody.setText(mess);
		p2pReqBody.setMessageId(messId);
		// 创建数据包
		ShowcasePacket reqPacket = new ShowcasePacket();
		reqPacket.setType(Type.P2P_RESP);
		reqPacket.setBody(Json.toJson(p2pReqBody).getBytes(ShowcasePacket.CHARSET));
		// 获取通道，发送
		Aio.sendToUser(groupContext, userId, reqPacket);
		// Aio.synSend(groupContext, reqPacket, 3000);
		// 等待 messId redis取
		String resultMess = "超时";
		for (int i = 0; i < 3; i++) {
			Thread.sleep(500);
			resultMess = (String) RedisUtil.getKey(messId);
			if (!("超时".equals(resultMess))) {
				break;
			}
		}
		return resultMess==null?"超时":resultMess;
	}

	/**
	 * 
	 * @param toUser
	 *            接收方
	 * @param textBody
	 *            发送的内容
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public boolean sendMessage(String toUser, String textBody, String messId) throws UnsupportedEncodingException {
		ServerGroupContext groupContext = ShowcaseServerStarter.serverGroupContext;
		System.out.println("发送消息。。。。。。");
		// 创建数据包
		// 创建响应实体 分别设置消息类型，消息内容
		P2PReqBody p2pReqBody = new P2PReqBody();
		p2pReqBody.setToUserid(toUser);//
		p2pReqBody.setText(textBody);
		p2pReqBody.setMessageId(messId);

		ShowcasePacket reqPacket = new ShowcasePacket();
		reqPacket.setType(Type.P2P_RESP);
		reqPacket.setBody(Json.toJson(p2pReqBody).getBytes(ShowcasePacket.CHARSET));
		// 获取通道，发送
		return Aio.sendToUser(groupContext, toUser, reqPacket);
	}

	/**
	 * 刷新页面
	 */
	public void plushData() throws UnsupportedEncodingException {
		System.out.println("进来读取数据");
		String respMess = RedisUtil.getKey("server").toString();
		RedisUtil.jedis.del("server");
		if (null != respMess || (!"".equals(respMess))) {
			renderJson("{\"successMsg\":\"用户忙.........\"}");
		}
		renderJson("{\"successMsg\":\"" + respMess + "\"}");
	}

	/**
	 * 前段传用户名，信息 在方法内封装json 添加fromUserid----uuid
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void doOne() throws UnsupportedEncodingException {
		// 创建数据包
		// 创建响应实体 分别设置消息类型，消息内容
		// 获取通道，发送
		// return Aio.sendToUser(channelContext, toUser, reqPacket);

		// Iterator it = json.keys();
		// while(it.hasNext()){
		// String key = (String) it.next();
		// //得到value的值
		// Object value = json.get(key);
		// //System.out.println(value);
		// if(value instanceof JSONObject)
		// {
		// stObj.push((JSONObject)value);
		// //递归遍历
		// JsonToMap(stObj,resultMap);
		// }
		// else {
		// resultMap.put(key, value);
		// }
		// }
		// renderJson(json);
	}

}
