package com.dawei.common.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 处理request  生成CSESSIONID
 * @author win7
 *
 * 
 *
 *
 */

public class RequestUtils {
	
	//得到csessionId 然后通过response写回到cookie中
	public static String getCsessionId(HttpServletRequest request,HttpServletResponse response){
		String cssionid="";
		
		//获取cookie
		Cookie[] cookies = request.getCookies();
		if(cookies!=null && cookies.length>0){
			
			for (Cookie cookie : cookies) {
				//从cookie中找CESSIONID
				if(cookie.getName().equals("CSESSIONID")){
					return cookie.getValue();
				}
				
			}
			
			
		}
		//如果cookie中没有  则创建一个   并写回cookie
		String newsessionId = UUID.randomUUID().toString().replaceAll("-", "");
		
		//放入Cookie
		Cookie cookie = new Cookie("CSESSIONID",newsessionId);
		//设置路径
		cookie.setPath("/");
		
		//设置cookie的存活时间    -1  关闭浏览器就销毁   0 立即销毁  >0 到指定时间后销毁
		cookie.setMaxAge(-1);   //关闭浏览器就销毁
		
		response.addCookie(cookie);
		 
		
		return newsessionId;
		
	}
	
}
