package jfinal.commom.intf;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
public class Tools {
	public static final String SERVERID="server";
	public static String getUuId(){
		UUID uid= UUID.randomUUID();
		return uid.toString().replace("-", "");
	}
}
