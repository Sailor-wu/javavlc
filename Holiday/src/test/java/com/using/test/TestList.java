package com.using.test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import com.holiday.easybots.controller.HolidayController;

public class TestList {

	@Test
	public void test() throws ParseException {
		HolidayController controller=new HolidayController();
		controller.getData();
	}

}
