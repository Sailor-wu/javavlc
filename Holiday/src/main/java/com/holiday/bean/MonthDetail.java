package com.holiday.bean;

import java.util.List;

public class MonthDetail {
	private String year;
	private String year_month;
	private List<Month> holiday;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getYear_month() {
		return year_month;
	}
	public void setYear_month(String year_month) {
		this.year_month = year_month;
	}
	public List<Month> getHoliday() {
		return holiday;
	}
	public void setHoliday(List<Month> holiday) {
		this.holiday = holiday;
	}
	
}
