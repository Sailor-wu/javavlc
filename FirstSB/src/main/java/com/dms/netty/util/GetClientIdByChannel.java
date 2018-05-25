package com.dms.netty.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.dms.netty.common.NyCurrentChannelMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class GetClientIdByChannel {
	public static String getClient(ChannelHandlerContext ctx){
		
		String clientId="";
		Channel channel= ctx.channel();
		Set set=NyCurrentChannelMap.map.entrySet();  
		Iterator it=set.iterator();  
		while(it.hasNext()) {  
		   Map.Entry entry=(Map.Entry)it.next();  
		   if(entry.getValue()==channel) {  
		     System.out.println(entry.getKey());
		     clientId=(String) entry.getKey();
		   }  
		} 
		return clientId;
	}
	
	public  Channel getChannelByClient(String clientid){
		return NyCurrentChannelMap.get(clientid);
	}
}
