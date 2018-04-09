package com.holiday.easybots.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.holiday.easybots.bean.Holiday;
import com.holiday.easybots.service.HolidayService;
import com.jfinal.core.Controller;

public class HolidayController extends Controller {

	int endDate = 20181231;// 结束时间
	int startYear = 0;// 开始年份
	private HolidayService service = new HolidayService();

	private List<String> holidays = new ArrayList<>();
	private List<String> dataWeek = new ArrayList<>();

	public void index() {
	}

	/**
	 * 获取所有的星期六天
	 * @throws ParseException 
	 */
	void getAllWeek() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String startTime = startYear + "0101";
		Date date = df.parse(startTime);
		int day = date.getDay();
		int startSatOffset = 6 - day;
		if (day == 0) {
			System.out.println("此年的第一天是星期天");
		}
		for (int i = 0; i <= 365 / 7; i++) {
			Date satday = df.parse(startYear + "010" + (1 + startSatOffset + i * 7));
			Date sunday = df.parse(startYear + "010" + (1 + startSatOffset + (i * 7 + 1)));
			System.out.println(df.format(satday) + "----" + df.format(sunday));
			dataWeek.add(df.format(satday));
			dataWeek.add(df.format(sunday));
		}
	}

	/**
	 * 根据接口获取全年的假日信息 1 到 12 月
	 */
	void getAllYearHoliday(int year) {
		String httpUrl = "http://www.easybots.cn/api/holiday.php?m=";
		BufferedReader reader = null;
		StringBuffer sbf = null;
		String dataFix = year + "01";
		int monthStart = Integer.valueOf(dataFix);// 开始的月份
		for (int k = 0; k < 12; k++) {
			String result = null;
			sbf = new StringBuffer("");
			try {
				dataFix = monthStart + "";
				URL url = new URL(httpUrl + dataFix);
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
				Map<String, Object> map = JSON.parseObject(result);
				System.out.println("结果集：" + map);
				// 处理json
				JSONObject object = (JSONObject) map.get(dataFix);
				String sb = null;
				sb = object.toString().replace(",", "},{");
				JSONArray jsonArray = JSONArray.parseArray("[" + sb + "]");
				System.out.println(jsonArray.size());
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					System.out.println("obj=====" + obj);
					Iterator it = obj.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						Object key = entry.getKey();
						Object value = entry.getValue();
						System.out.println("key=" + key + " value=" + value);
						String special_date = dataFix + key;
						// DateFormat df = new SimpleDateFormat("yyyy-M-d");
						// special_date=df.format(special_date);
						holidays.add(special_date);// 拿到节假日
					}
				}
				// 判断节假日信息里面是否存在周六日
				// for (int i = 0; i < holidays.size(); i++) {
				// for (int j = 0; j < dataWeek.size(); j++) {
				//
				// //获取节假日和周六日比较，不是周六日的话就是法定休息日，需要提取出来保存数据库
				// if(holidays.get(i).equals(dataWeek.get(j))){
				// //找到了就是星期六天了，不管
				// break;
				// }else if(j==dataWeek.size()-1){
				// //朝赵到最后，没有发现就保存数据库
				// Holiday holiday = new Holiday();
				// String special_date=holidays.get(i);
				// holiday.put("special_date",special_date);
				// holiday.put("date_type","节假日");
				// holiday.save();
				// }
				// }
				//
				// }
				// 判断获取到的周六日是否有假期，没有就是要上班

			} catch (Exception e) {
				e.printStackTrace();
			}
			monthStart++;
		}

	}

	/**
	 * 页面请求处理方法
	 * @throws ParseException 
	 */
	public void getData() throws ParseException {
		startYear = getParaToInt("time");
		getAllWeek();// 加载所有的星期六天
		getAllYearHoliday(startYear);// 初始化加载所有假期信息
		HashSet<String> set = new HashSet<>();
		set.addAll(holidays);
		set.addAll(dataWeek);
		// 分别取到两部分的独有
		// dateWeek独有的部分
		ArrayList<String> listA = new ArrayList<>(set);
		listA.removeAll(holidays);
		Collections.sort(listA);
		StringBuffer buffer = new StringBuffer();
		for (String string : listA) {
			int num = Integer.valueOf(string);
			if (endDate >= num) {
				Holiday holiday = new Holiday();
				System.out.println("这些是非工作日，但是要上班的数据：" + num);
				buffer.append("{\"" + num + "\",1}");
				holiday.put("special_date", num);
				holiday.put("date_type", 1);
				holiday.save();
			}
		}
		// //holidays独有的部分
		ArrayList<String> listB = new ArrayList<>(set);
		listB.removeAll(dataWeek);
		Collections.sort(listB);
		for (String string : listB) {
			int num = Integer.valueOf(string);
			if (endDate >= num) {
				System.out.println("这些是非星期六天，但是可以休息的数据：" + num);
				Holiday holiday = new Holiday();
				buffer.append("{\"" + num + "\",2}");
				holiday.put("special_date", num);
				holiday.put("date_type", 2);
				holiday.save();
			}
		}
		HashSet<String> setResult = new HashSet<>();
		setResult.addAll(listA);
		setResult.addAll(listB);
		ArrayList<String> listResult = new ArrayList<>(setResult);
		renderJson(buffer.toString());
	}

	/**
	 * 判断时间是不是星期六天 如果返回true 就是正常的
	 * 
	 * @param checkDate
	 * @return
	 * @throws ParseException
	 */
	public boolean checkWeek(String checkDate) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse("2018-01-01");
		int day = date.getDay();
		int startSatOffset = 6 - day;
		if (day == 0) {
			System.out.println("此年的第一天是星期天");
		}
		for (int i = 0; i <= 365 / 7; i++) {
			Date satday = df.parse("2018-01-" + (1 + startSatOffset + i * 7));
			Date sunday = df.parse("2018-01-" + (1 + startSatOffset + (i * 7 + 1)));
			if (checkDate.equals(df.format(satday)) || checkDate.equals(df.format(sunday))) {
				return true;
			}
		}
		return false;
	}

//	static {
//		try {
//			DateFormat df = new SimpleDateFormat("yyyyMMdd");
//			Date date = df.parse("20180101");
//			int day = date.getDay();
//			int startSatOffset = 6 - day;
//			if (day == 0) {
//				System.out.println("此年的第一天是星期天");
//			}
//			for (int i = 0; i <= 365 / 7; i++) {
//				Date satday = df.parse("2018010" + (1 + startSatOffset + i * 7));
//				Date sunday = df.parse("2018010" + (1 + startSatOffset + (i * 7 + 1)));
//				System.out.println(df.format(satday) + "----" + df.format(sunday));
//				dataWeek.add(df.format(satday));
//				dataWeek.add(df.format(sunday));
//			}
//		} catch (Exception e) {
//		}
//
	}
	// Collection ret = CollectionUtils.intersection(list1, list2);
	// Collection union = CollectionUtils.union( a, b ); //并集
	// Collection intersection = CollectionUtils.intersection( a, b ); //交集
	// Collection disjunction = CollectionUtils.disjunction( a, b ); //析取
	// Collection subtract = CollectionUtils.subtract( holidays,dataWeek ); //差集
	// //判断节假日信息里面是否存在周六日,如果存在周六日，那么就是正常的，如果不是周六日，那么就是法定的假日
	// for (int i = 0; i < holidays.size(); i++) {
	// //循环的节假日和周六日比较,不是周六日的保存
	// for (int j = 0; j < dataWeek.size(); j++) {
	// if(!holidays.get(i).equals(dataWeek.get(j))){
	// Holiday holiday=new Holiday();
	// holiday.put("special_date",holidays.get(i));
	// holiday.put("date_type", "法定节假日");
	// holiday.save();
	// break;
	// }else{
	// continue;
	// }
	// }
	// }
	// //可能调休了
	// //调休了之后，周六日要上班，并且在节假日里面是不存在的，这些日子要记下来 （ 要上班怎么表示？？？？）
	// for (int i = 0; i < dataWeek.size(); i++) {
	// for (int j = 0; j < holidays.size(); j++) {
	// if(!(dataWeek.get(i).equals(holidays.get(j)))){
	// Holiday holiday=new Holiday();
	// holiday.put("special_date",dataWeek.get(i));
	// holiday.put("date_type", "工作日");
	// holiday.save();
	// break;
	// }else{
	// continue;
	// }
	// }
	//
	// }
	// String httpUrl = "http://www.easybots.cn/api/holiday.php?m=";
	// BufferedReader reader = null;
	// String result = null;
	// StringBuffer sbf = new StringBuffer();
	// String dataFix = "201801";
	// try {
	// URL url = new URL(httpUrl + dataFix);
	// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	// connection.setRequestMethod("GET");
	// connection.connect();
	// InputStream is = connection.getInputStream();
	// reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	// String strRead = null;
	// while ((strRead = reader.readLine()) != null) {
	// sbf.append(strRead);
	// sbf.append("\n");
	// }
	// reader.close();
	// result = sbf.toString();
	// Map<String, Object> map = JSON.parseObject(result);
	// System.out.println("结果集：" + map);
	// // 处理json
	// JSONObject object = (JSONObject) map.get(dataFix);
	// String sb = null;
	// sb = object.toString().replace(",", "},{");
	// JSONArray jsonArray = JSONArray.parseArray("[" + sb + "]");
	// System.out.println(jsonArray.size());
	// for (int i = 0; i < jsonArray.size(); i++) {
	// JSONObject obj = jsonArray.getJSONObject(i);
	// System.out.println("obj=====" + obj);
	// Iterator it = obj.entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry entry = (Map.Entry) it.next();
	// Object key = entry.getKey();
	// Object value = entry.getValue();
	// System.out.println("key=" + key + " value=" + value);
	// String special_date = dataFix + key;
	// holidays.add(special_date);// 拿到节假日
	// }
	// }
	// //判断节假日信息里面是否存在周六日
	// for (int i = 0; i < holidays.size(); i++) {
	// for (int j = 0; j < dataWeek.size(); j++) {
	//
	// //获取节假日和周六日比较，不是周六日的话就是法定休息日，需要提取出来保存数据库
	// if(holidays.get(i).equals(dataWeek.get(j))){
	// //找到了就是星期六天了，不管
	// break;
	// }else if(j==dataWeek.size()-1){
	// //朝赵到最后，没有发现就保存数据库
	// Holiday holiday = new Holiday();
	// String special_date=holidays.get(i);
	// holiday.put("special_date",special_date);
	// holiday.put("date_type","节假日");
	// holiday.save();
	// }
	// }
	//
	// }
	// //判断获取到的周六日是否有假期，没有就是要上班
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// renderJson();

	// 把节假日不是星期六天的保存下来，那么就是要上班的星期六天了 ，这就是非正常上班

	// 判断节假日是不是星期六天，不是的保存进去，说明这是非正常休息
	/*
	 * if (!checkWeek(special_date)) { holiday.put("special_date",
	 * special_date); holiday.put("date_type", "法定节假日"); holiday.save(); }
	 */

	/*
	 * public static void main(String[] args) { for (String string :
	 * HolidayController.dataWeek) { System.out.println("信息：" + string); } }
	 */
//}
