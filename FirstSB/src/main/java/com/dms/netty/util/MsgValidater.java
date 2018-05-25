package com.dms.netty.util;
import com.alibaba.fastjson.JSONObject;

/**
 * @author wangbing 2017年12月16日 10:59:12
 * 对服务端收到的消息进行校验，包括数据校验和连接可用性进行校验
 */

public class MsgValidater {
	private String acceptMsg ;
	
	private String responseMsg;
	
	public MsgValidater(String msg){
		this.acceptMsg = msg;
		
	}
	//建立tcp连接接时对连接消息的有效性进行校验
	public boolean connectValid(){
		//创建连接是的json语句需要，对cmd_type 是否是 tcp_connect ,对authorizer_appid 进行校验是否存在
		if(!jsonValid()){
			return false;
		}
		JSONObject data1json = new JSONObject();
		data1json = JSONObject.parseObject(acceptMsg);
		//命令方式，是连接还是，发送数据
		String cmdType = data1json.getString("cmd_type");
		if(!"tcp_connect".equals(cmdType)){
			return false;
		}
		//收取appId
		String authAppid = data1json.getString("authorizer_appid");
		return true;
	}
	//检查是否是正常的Json数据消息
	public boolean checkJsonMsg(){
		return jsonValid();
	}
	
	//对接受的json数据进行校验
	private boolean jsonValid(){
		return new JsonValidator().validate(acceptMsg);
	}
	//添加toString方法，输出值
	public String toString(){
		return acceptMsg;
	}
}
