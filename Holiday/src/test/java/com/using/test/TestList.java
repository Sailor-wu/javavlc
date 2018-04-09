package com.using.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.holiday.easybots.controller.HolidayController;

public class TestList {

	@Test
	public void test() {
		HolidayController controller=new HolidayController();
		controller.getData();
	}

}
