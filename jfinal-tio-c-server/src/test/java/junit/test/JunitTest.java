package junit.test;

import static org.junit.Assert.*;

import org.junit.Test;

import jfinal.commom.intf.Tools;


public class JunitTest {

	@Test
	public void test() {
		System.out.println(Tools.getUuId());
		String  mess="123";
		System.out.println(null!=mess);
		System.out.println(!("".equals(mess))&&null!=mess);
		System.out.println("".equals(mess));
		System.out.println(1>2?3:4);
	}
}
