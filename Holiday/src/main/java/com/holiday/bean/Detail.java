package com.holiday.bean;

import java.util.Date;

/**
 * 每日详情
 * @author why
 * @时间 2018年4月8日
 */
public class Detail {

	private Date date;//当前日期
	private String weekday;//当前礼拜
	private String animalsYear;//当前属性年 （鼠牛虎兔）
	private String suit;//合适事宜
	private String avoid;//避开禁忌
	private String year_month;//月份
	private String lunar;//农历
	private String lunarYear;//太阴年
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getWeekday() {
		return weekday;
	}
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	public String getAnimalsYear() {
		return animalsYear;
	}
	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}
	public String getSuit() {
		return suit;
	}
	public void setSuit(String suit) {
		this.suit = suit;
	}
	public String getAvoid() {
		return avoid;
	}
	public void setAvoid(String avoid) {
		this.avoid = avoid;
	}
	public String getYear_month() {
		return year_month;
	}
	public void setYear_month(String year_month) {
		this.year_month = year_month;
	}
	public String getLunar() {
		return lunar;
	}
	public void setLunar(String lunar) {
		this.lunar = lunar;
	}
	public String getLunarYear() {
		return lunarYear;
	}
	public void setLunarYear(String lunarYear) {
		this.lunarYear = lunarYear;
	}
	
	
}
