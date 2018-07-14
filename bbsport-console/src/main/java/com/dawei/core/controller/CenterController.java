package com.dawei.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawei.core.pojo.TestDB;
import com.dawei.vore.service.TestTbService;

//后台管理重心

@Controller
@RequestMapping(value="/control")
public class CenterController {
	
	//后台的入口页面跳转   /control/index.do
	
	@RequestMapping(value="/index.do")
	public String index(){
		return "index";
	}
	
	
	@RequestMapping(value="/top.do")
	public String top(){
		//显示头页面
		return "top";
	}
	
	
	@RequestMapping(value="/main.do")
	public String main(){
		//显示主页面
		return "main";
	}
	
	
	@RequestMapping("/left.do")
	public String left(){
		//显示主页面中的左页面
		return "left";
	}
	
	@RequestMapping("/right.do")
	public String right(){
		//显示主页面中的右页面
		return "right";
	}
	
	//商品身体
	
	
	@RequestMapping("/frame/product_main.do")
	public String product_main(){
		//商品身体
		return "frame/product_main";
	}
	
	@RequestMapping("/frame/product_left.do")
	public String product_left(){
		//显示主页面中的右页面
		return "frame/product_left";
	}
	
	
	
	
	
	//测试
//	@Autowired 
//	public TestTbService testTbService;
//	@RequestMapping(value = "/test/index.do")
//	public String index(){
//		System.out.println("index......");
//		TestDB td = new TestDB();
//		td.setName("周润发");
//		td.setBirthday(new Date());
//		testTbService.insertTestDb(td);
//		return "index";
//	}
	
}
