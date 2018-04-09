package com.holiday.commom;

import com.holiday.controller.IndexController;
import com.holiday.easybots.bean.Holiday;
import com.holiday.easybots.controller.HolidayController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

public class CommomConfig extends JFinalConfig {

	/**
	 *JFinal常量值，如开发模式常量 devModel，默认视图配置 ViewType 
	 */
	@Override
	public void configConstant(Constants me) {
		//配置开发模式
		me.setDevMode(true);
//		me.setViewType(ViewType.FREE_MARKER);
	}

	/**
	 * 配置JFinal访问路由，
	 	如 re.add("/hello",HelloController.class)将"/hello"映射到HelloController上
	 	Route类的两个方法 
		public Routes add(String controllerKey, Class<? extends Controller> controllerClass, String viewPath)
		public Routes add(String controllerKey, Class<? extends Controller> controllerClass)
		JFinal的路由规则
		url组成	访问目标
		controllerKey Controller.index();
		controllerKey/v0-v1	Controller.index(),所带url参数值为 v0-v1
		controllerKey/method	Controller.method();
		controllerKey/method/vo-v1	Controller.method(),所带url参数值为v0-v1
		即默认是index方法来处理请求，JFinal默认使用"-"来分隔多个值
		controllerKey、method、urlPara 必须使用"/"来分隔
		也可在方法上使用,如 ActionKey("/login/112-1123") 类似的配置 来更改路由配置
	 */
	@Override
	public void configRoute(Routes me) {
		//添加路由映射规则  把hello请求发送到HelloController类处理
//		me.add("/", IndexController.class,"/index");
		me.add("/",IndexController.class,"/index");
		me.add("/holiday",HolidayController.class);
//		me.add("/tuser", TUserController.class);
	}

	/**
	 * 配置模板引擎
	 */
	@Override
	public void configEngine(Engine me) {
//		me.setBaseTemplatePath(null);
//		me.setSourceFactory(new ClassPathSourceFactory());
	}

	/**
	 * 配置c3p0数据库连接池插件，ActiveRecord数据访问插件
		JFinal插件架构是其主要扩展方式之一，可以方便地创建插件并应用到项目中
	 */
	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost/holiday",
				"why", "123456");
				me.add(dp);//添加阿里巴巴的数据源插件
				ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
				me.add(arp);//添加ActiveRecord数据库操作插件
				//关联数据库和实体类信息  t_user数据库表   TUser.class实体类
//				arp.addMapping("holiday", Holiday.class);
				arp.addMapping("holiday", Holiday.class);
	}
	/**
	 * 用来配置JFinal的全局拦截器，全局拦截器拦截所有action请求，除非使用 "@clear"注解在Controller中清除
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		
	}

	/**
	 * Handler可以接管所有web请求
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("contextPath"));
	}
}
