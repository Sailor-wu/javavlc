package com.holiday.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.holiday.bean.Detail;
import com.holiday.bean.Month;
import com.holiday.bean.MonthDetail;
import com.holiday.bean.Year;
import com.holiday.commom.GetDate;

public class TestHoliday {
	/**
	 * 测试
	 * @param httpArg
	 * @return
	 */
	public static String request(String httpArg) {
//		String httpUrl = "http://v.juhe.cn/calendar/year";
		String httpUrl="http://v.juhe.cn/calendar/month";
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		//year-month    or year
		httpUrl = httpUrl + "?year-month=" + httpArg + "&key=d19cb6cb44a20ba71cccebf7e8fe71e4";
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
			String res = (String) map.get(httpArg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
//		GetYear();
//		getMonth("2018-4");
		GetDate getDate=new GetDate();
//		Scanner in=new Scanner(System.in);
//		SimpleDateFormat f = new SimpleDateFormat("yyyy");
//		System.out.println("输入年份：");
//		String inyear=in.nextLine();
//		System.out.println("信息：");
//		for (Year year : getDate.getYear(inyear)) {
//			System.out.println("节日名字："+year.getName()+"节日开始时间："+year.getStartday());
//		} 
		
		MonthDetail detail= getDate.getMonth("2018-5");
		System.out.println(detail.getYear_month());
		System.out.println(detail.getHoliday().size());
		for (Month month : detail.getHoliday()) {
			System.out.println(month.getName()+"休息："+month.getDesc()+"\n"+month.getFestival()+"\n"+month.getRest());
			System.out.println();
		}
		
//		Detail detail = getDate.getDetail("2018-4-9");
//		System.out.println(detail.getLunarYear()+"\n"+detail.getAnimalsYear()+"\n"+detail.getAvoid()+"\n"+detail.getLunar()+"\n"+detail.getSuit()+"\n"+detail.getWeekday()+"\n"+detail.getYear_month());
	}

	/**
	 * 测试
	 * @param string
	 */
	private static void getMonth(String string) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-M");
		String httpArg = f.format(new Date());
		System.out.println(httpArg);
		String jsonResult = request(httpArg);
		// Map<String,Object> map=JsonUtil.toMap(jsonResult);
		Map<String, Object> map = JSON.parseObject(jsonResult);
		System.out.println("map"+map);
		JSONObject object = (JSONObject) map.get("result");
		
		System.out.println("result节点:" + object);
		JSONObject obdata = (JSONObject) object.get("data");
		System.out.println("data:" + obdata);
		String obyesr = (String) obdata.get("year");
		System.out.println("obyesr:" + obyesr);
		JSONArray jsonArray = obdata.getJSONArray("holiday");
		System.out.println("数组信息：" + jsonArray.toString());
		JSONArray arr = JSONArray.parseArray(jsonArray.toString());
		System.out.println(arr);
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			System.out.println("日期："+obj.get("startday")+"---节日："+obj.get("name"));
//			System.out.println(obj.toString());
		}
	}

	/**
	 * 测试
	 */
	private static void GetYear() {
		// 判断今天是否是工作日 周末 还是节假日
		SimpleDateFormat f = new SimpleDateFormat("yyyy");
		String httpArg = f.format(new Date());
		System.out.println(httpArg);
		String jsonResult = request(httpArg);
		// Map<String,Object> map=JsonUtil.toMap(jsonResult);
		Map<String, Object> map = JSON.parseObject(jsonResult);
		JSONObject object = (JSONObject) map.get("result");
		System.out.println("result节点:" + object);
		JSONObject obdata = (JSONObject) object.get("data");
		System.out.println("data:" + obdata);
		String obyesr = (String) obdata.get("year");
		System.out.println("obyesr:" + obyesr);
		JSONArray jsonArray = obdata.getJSONArray("holidaylist");
		// JSONObject objectHolidays=(JSONObject) obdata.get("holidaylist");
		System.out.println("数组信息：" + jsonArray.toString());
		JSONArray arr = JSONArray.parseArray(jsonArray.toString());
		System.out.println(arr);
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			System.out.println("日期："+obj.get("startday")+"---节日："+obj.get("name"));
//			System.out.println(obj.toString());
		}
	}
}
