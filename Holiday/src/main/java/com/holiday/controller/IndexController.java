package com.holiday.controller;

import java.text.SimpleDateFormat;

import com.jfinal.core.Controller;

public class IndexController extends Controller{
	public void index(){
		
	}

	public void getData(){
		String data=getPara("time");
		System.out.println("数据："+data);
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
//		format.format(date);
		renderJson("");
	}
}
