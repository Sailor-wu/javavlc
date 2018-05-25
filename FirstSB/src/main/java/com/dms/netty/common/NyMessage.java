package com.dms.netty.common;


public class NyMessage {
	private String clientId;// 发送者客户端ID

	private NyMsgType type;// 消息类型

	private String data;// 数据

	private String targetId;// 目标客户端ID

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public NyMsgType getType() {
		return type;
	}

	public void setType(NyMsgType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public NyMessage(){  
	          
	    }

	public NyMessage(NyMsgType type){  
	        this.type = type;  
	    }

	public NyMessage(String clientId, NyMsgType type, String data, String targetId) {
		this.clientId = clientId;
		this.type = type;
		this.data = data;
		this.targetId = targetId;
	}

	@Override
	public String toString() {
		return "{\"clientid\":\""+clientId+"\",\"data\":["+data+"],\"type\":\""+type.toString()+"\"}";
	}
	
	
}
