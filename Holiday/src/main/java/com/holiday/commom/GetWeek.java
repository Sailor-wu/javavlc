package com.holiday.commom;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.holiday.easybots.controller.HolidayController;

public class GetWeek {

	public static void main(String[] args) throws ParseException {
		// GetWeek();
		// DateFormat df = new SimpleDateFormat("yyyy-M-d");
		// Date date = df.parse("2018-4-1");
		// int day = date.getDay();
		// int startSatOffset = 6-day;
		// if(day==0){
		// System.out.println("此年的第一天是星期天");
		// }
		// for(int i=0;i<=365/7;i++){
		// Date satday = df.parse("2018-1-"+(1+startSatOffset+i*7));
		// Date sunday = df.parse("2018-1-"+(1+startSatOffset+(i*7+1)));
		// System.out.println(df.format(satday)+"----"+df.format(sunday));
		// }
		
	}

	private static void GetWeek() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();// 当前日期
		int year = calendar.get(Calendar.YEAR);// 2010
		int nextyear = calendar.get(Calendar.YEAR) + 1;// 2011;
		Calendar nowyear = Calendar.getInstance();
		Calendar nexty = Calendar.getInstance();
		nowyear.set(year, 0, 1);// 2010-1-1
		nexty.set(nextyear, 0, 1);// 2011-1-1

		calendar.add(Calendar.DAY_OF_MONTH, -calendar.get(Calendar.DAY_OF_WEEK));// 周六
		Calendar c = (Calendar) calendar.clone();
		for (; calendar.before(nexty) && calendar.after(nowyear); calendar.add(Calendar.DAY_OF_YEAR, -7)) {
			printf(calendar);
		}
		for (; c.before(nexty) && c.after(nowyear); c.add(Calendar.DAY_OF_YEAR, 7)) {
			printf(c);
		}
	}

	// 打印
	public static void printf(Calendar calendar) {
		System.out.println(calendar.get(Calendar.YEAR) + "-" + (1 + calendar.get(Calendar.MONTH)) + "-"
				+ calendar.get(Calendar.DATE));

	}
}
