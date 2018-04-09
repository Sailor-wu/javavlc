package com.holiday.easybots.com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSON;
/**
 * testMain
 * @author why
 * @时间 2018年4月9日
 */
public class Common {
	public static void main(String[] args) {
		//http://www.easybots.cn/api/holiday.php?d=20130101
		//http://www.easybots.cn/api/holiday.php?d=20130101,20130103,20130105,20130201
		String httpUrl="http://www.easybots.cn/api/holiday.php?m=201801";
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		//year-month    or year
//		httpUrl = httpUrl + "?d=20180405";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\n");
			}
			reader.close();
			result = sbf.toString();
			// Map<String,Object> map=JsonUtil.toMap(result);
			Map<String, Object> map = JSON.parseObject(result);
//			String res = (String) map.get(httpArg);
			System.out.println("结果集："+map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
