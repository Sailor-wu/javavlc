package com.dms.netty.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

public class NyCurrentChannelMap {
	
	public final static Map<String, Channel> map = new ConcurrentHashMap<>();

	public static void add(String clientId, Channel channel) {
		map.put(clientId, channel);
	}

	public static Channel get(String clientId) {
		return map.get(clientId);
	}

	public static void remove(Channel channel) {
		for (Map.Entry<String, Channel> entry : map.entrySet()) {
			if (entry.getValue() == channel) {
				map.remove(entry.getKey());
			}
		}
	}
}