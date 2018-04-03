package jfinal.commom.util;
import redis.clients.jedis.Jedis;
public class RedisUtil {
	public static Jedis jedis;
	static{
		// 连接本地的 Redis 服务
		jedis = new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		// 查看服务是否运行
		System.out.println("Server is running: " + jedis.ping());
//		jedis.set(TestRedis.SERVERID, "nomal123456");
//		System.out.println(jedis.get(TestRedis.SERVERID));
//		Map<String,String> maps=new HashMap<>();
//		maps.put("name", "张三");
//		maps.put("password", "zhangsan");
//		jedis.hmset("123",maps);
//		System.out.println(jedis.hmget("123", "password"));
	}
	
	public static Object getKey(String key) {
		return jedis.get(key);
	}
	
	public static void main(String [] aa){
//		jedis.set(TestRedis.SERVERID, "123456");
//		System.out.println(jedis.get(TestRedis.SERVERID));
	}
}
