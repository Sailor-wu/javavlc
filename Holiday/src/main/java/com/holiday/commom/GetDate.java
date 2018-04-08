package com.holiday.commom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.holiday.bean.DateAndStatus;
import com.holiday.bean.Detail;
import com.holiday.bean.Month;
import com.holiday.bean.MonthDetail;
import com.holiday.bean.Year;

/**
 * 获取信息
 * @author why
 * @时间 2018年4月8日
 */
public class GetDate {
	private static final String KEY="d19cb6cb44a20ba71cccebf7e8fe71e4";
	private static String HTTPURL_DAY="http://v.juhe.cn/calendar/day";
	private static String HTTPURL_MONTH="http://v.juhe.cn/calendar/month";
	private static String HTTPURL_YEAR="http://v.juhe.cn/calendar/year";
	/**
	 * 日期转换器  由String 类型转为Date类型
	 * @param date
	 * @throws ParseException
	 */
	public  Date simPleDateForString(String date) throws ParseException{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-M");
		return  format.parse(date);
	}
	/**
	 * 获取全年的假期信息
	 * @param year
	 * @return
	 */
	public List<Year> getYear(String year){
		//通过年份去获取
		return getByYear(year);
	}
	private List<Year> getByYear(String year) {
		List<Year> years=new ArrayList<>();
//		String httpUrl="http://v.juhe.cn/calendar";
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
//		httpUrl = httpUrl + "/year?year=" + year + "&key="+KEY;
		try {
			URL url = new URL(HTTPURL_YEAR+"?year=" + year + "&key="+KEY);
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
			//处理json
			JSONObject object = (JSONObject) map.get("result");
			System.out.println("result节点:" + object);
			JSONObject obdata = (JSONObject) object.get("data");
			System.out.println("data:" + obdata);
			String obyesr = (String) obdata.get("year");
			System.out.println("obyesr:" + obyesr);
			JSONArray jsonArray = obdata.getJSONArray("holidaylist");
			System.out.println("数组信息：" + jsonArray.toString());
			JSONArray arr = JSONArray.parseArray(jsonArray.toString());
			System.out.println(arr);
			for (int i = 0; i < arr.size(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				Year year2=new Year();
				year2.setName((String) obj.get("name"));
				year2.setStartday((String) obj.get("startday"));
				years.add(year2);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return years;
	}
	/**
	 * 通过月份获取整个月的假期信息
	 * @param month
	 * @return
	 */
	public  MonthDetail getMonth(String month){
		return getByMonth(month);
	}
	private MonthDetail getByMonth(String month) {
		MonthDetail months=new MonthDetail();
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(HTTPURL_MONTH+"?year-month=" + month + "&key="+KEY);
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
			//处理json
			JSONObject object = (JSONObject) map.get("result");
			JSONObject obdata = (JSONObject) object.get("data");
			String obyesr = (String) obdata.get("year");
			months.setYear(obyesr);
			months.setYear_month((String) obdata.get("year-month"));
			JSONArray jsonArray = obdata.getJSONArray("holiday");
			JSONArray arr = JSONArray.parseArray(jsonArray.toString());
			List<Month> monthDetails=new ArrayList<>();
			for (int i = 0; i < arr.size(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				Month detail=new Month();
				detail.setRest(obj.getString("rest"));
				detail.setName(obj.getString("name"));
				detail.setFestival(obj.getString("festival"));
				detail.setDesc(obj.getString("desc"));
				JSONArray jsonList = obj.getJSONArray("list");
				JSONArray list = JSONArray.parseArray(jsonList.toString());
				List<DateAndStatus> dateAndStatusList=new ArrayList<>();
				for (int j = 0; j < list.size(); j++) { 
					DateAndStatus dateAndStatus=new DateAndStatus();
					JSONObject objList = list.getJSONObject(j);
					dateAndStatus.setDate(simPleDateForString(objList.getString("date")));
					dateAndStatus.setStatus(objList.getString("status"));
					dateAndStatusList.add(dateAndStatus);
					detail.setList(dateAndStatusList);
				}
				monthDetails.add(detail);
			}
			months.setHoliday(monthDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return months;
	}
	/**
	 * 根据具体的日期获取当前日期的具体信息
	 * @param day
	 * @return
	 */
	public Detail getDetail(String day){
		return  getDetailByDay(day);
	}
	private Detail getDetailByDay(String day) {
		Detail detail=new Detail();
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(HTTPURL_DAY+"?date=" + day + "&key="+KEY);
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
			//处理json
			JSONObject object = (JSONObject) map.get("result");
			JSONObject obdata = (JSONObject) object.get("data");
			detail.setDate(simPleDateForString(obdata.getString("date")));
			detail.setAvoid(obdata.getString("avoid"));
			detail.setAnimalsYear(obdata.getString("animalsYear"));
			detail.setLunar(obdata.getString("lunar"));
			detail.setLunarYear(obdata.getString("lunarYear"));
			detail.setSuit(obdata.getString("suit"));
			detail.setWeekday(obdata.getString("weekday"));
			detail.setYear_month(obdata.getString("year-month"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}
}
