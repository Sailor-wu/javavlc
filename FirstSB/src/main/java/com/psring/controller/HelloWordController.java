package com.psring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * 控制器
 * @author Administrator
 * @RestController和 @RequestMapping朱姐来自SpringMVC 而不是来自springboot
 * @RestController: 提供实现了Rest api 可以服务JSON XML或者其他。也可以以String的形式渲染结果
 */

import com.psring.entity.Person;
@RestController
public class HelloWordController {

	
	@Autowired
	private ApplicationContext ctx;
	
	@RequestMapping("/")
	public  String helloWord(){
		return "Hello Word!";
	}
	/**
	 * REST 风格  方法 GET 已JSON形式返回
	 * @return
	 */
	@RequestMapping(value="/person/{id}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public Person getPerson(){
		Person person=new Person();
		person.setId(123456);
		person.setAge(20);
		person.setName(ctx.getEnvironment().getProperty("test.user.name"));
		return person;
	}
	
}
