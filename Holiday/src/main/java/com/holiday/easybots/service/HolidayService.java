package com.holiday.easybots.service;

import java.util.List;

import com.holiday.easybots.bean.Holiday;
import com.jfinal.plugin.activerecord.Page;

public class HolidayService {

	private static final Holiday holidayDao=new Holiday().dao();

	public boolean saveTUser(Holiday holiday){
		return holiday.save();
	}
	
	public Page<Holiday> getAllUsers(Integer pageNo,Integer pageSize){
		return holidayDao.paginate(pageNo, 
				pageSize, "select *"," from t_user order by id desc ");
	}
	
	public  boolean deleteTuser(int id){
		return holidayDao.deleteById(id);
	}
	
	public boolean updateTuser(Holiday holiday){
		return holiday.update();
	}
	
	public Holiday findById(int id){
		return holidayDao.findById(id);
	}
}
