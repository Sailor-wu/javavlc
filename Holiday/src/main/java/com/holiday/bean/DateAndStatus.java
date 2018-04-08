package com.holiday.bean;
import java.util.Date;
/**
 * 日期和状态
 * @author why
 * @时间 2018年4月8日
 */
public class DateAndStatus {
	private Date date;//日期
	private String status;//状态  1 休息    2.正常上班
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
