package com.junit.sb;

import static org.junit.Assert.*;

import org.junit.Test;

import com.psring.controller.HelloWordController;

public class TestSpringBoot {

	@Test
	public void test() {
		assertEquals("Hello Word!",new HelloWordController().helloWord());
	}

}
