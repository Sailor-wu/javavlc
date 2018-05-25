package com.psring.startSB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 * @author Administrator
 * @SpringBootApplication: Spring Boot 应用的标识
 * Application 很简单，一个main函数作为主入口。SpringApplication引导应用，并将Application本身作为参数传递给run方法。
 * 具体的run方法会启动嵌入式的tomcat并初始化Spring环境极其各Spring组件
 * SpringBoot在写启动类的时候如果不使用@ComponentScan指明对象扫描范围，默认指扫描当前启动类所在的包里的对象.所以要指定具体的处理类所在的包下
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.psring.controller"})
public class StartSB {

	/*public static void main(String[] args) {
		SpringApplication.run(StartSB.class, args);
	}*/
	
//	public static void main(String[] args) {
//		new SpringApplicationBuilder(StartSB.class).properties("spring.config.location=classpath:/abc.properties").run(args);
//	}
	public static void main(String[] args) {
		new SpringApplicationBuilder(StartSB.class).properties("spring.profiles.active=window").run(args);
	}
}
