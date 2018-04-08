package com.holiday.bean;

import java.util.List;

/**
 * 月信息
 * @author why
 * @时间 2018年4月8日
 */
public class Month {
	private String rest;// 休息
	private String festival;// 节日
	private String name;// 节日名字
	private String desc;// 描述
	private List<DateAndStatus> list;

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public String getFestival() {
		return festival;
	}

	public void setFestival(String festival) {
		this.festival = festival;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<DateAndStatus> getList() {
		return list;
	}

	public void setList(List<DateAndStatus> list) {
		this.list = list;
	}

}
