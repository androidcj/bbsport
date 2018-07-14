package com.dawei.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dawei.common.utils.RequestUtils;
import com.dawei.core.pojo.user.Buyer;
import com.dawei.vore.service.buyer.BuyerService;
import com.dawei.vore.service.buyer.SessionProvider;

/**
 * 
 * 登录
 * @author win7
 *
 */

@Controller
public class LoginController {
	@Autowired
	private BuyerService buyerService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	
	//去登录页面
	@RequestMapping(value="/shopping/login.aspx",method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	
	//提交表单
	@RequestMapping(value="/shopping/login.aspx",method=RequestMethod.POST)
	public String login(String username,String password,String returnURL,Model model,HttpServletRequest request,HttpServletResponse response){
		//用户名密码不能为空
		System.out.println("username===="+username);
		System.out.println("password===="+password);
		
		
		if(username!=null){
			if(password!=null){
				//判断用户是否存在
				Buyer buyer = buyerService.selectUserByUsername(username);
				System.out.println("buyer===="+buyer.toString());
				if(buyer!=null){
					//验证密码是否正确
					
					System.out.println("加密后："+password);
					System.out.println("保存的密码:"+buyer.getPassword());
					if(encodePassword(password).equals(buyer.getPassword())){
//						key中保存cookie中 jsessionId
						sessionProvider.setAttribute(RequestUtils.getCsessionId(request, response), buyer.getUsername());
						//登录成功   重定向到returnURL
						return "redirect:"+returnURL;
					}else{
						model.addAttribute("error", "密码不正确");
					}
				}else{
					model.addAttribute("error", "用户不存在");
				}
				
			}else{
				model.addAttribute("error", "密码为空");
			}
		}else{
			model.addAttribute("error", "用户名为空");
		}
		
		return "login";
	}
	
	

	
	@RequestMapping(value="/shopping/isLogin.aspx")
	public @ResponseBody MappingJacksonValue isLogin(String callback,HttpServletRequest request,HttpServletResponse response){
		String username = sessionProvider.getAttribute(RequestUtils.getCsessionId(request, response));
		Integer result = 0;    //登录标识   0 未登录  1登录
		
		if(username!=null){
			//登录
			result = 1;
		}
		
		System.out.println("callback==="+callback);
		
//		response.setContentType("application/json;charset=utf-8");
//		try {
//			PrintWriter pw = response.getWriter();
//			pw.write(result);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//jsonp的返回
		
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		mjv.setJsonpFunction(callback);
		return mjv;
		
	}
	
	
	//加密
	public String encodePassword(String password){
		
		String algorithm = "MD5";
		char[] encode= null;
		try {
			MessageDigest md =  MessageDigest.getInstance(algorithm);
			byte[] bytess =  md.digest(password.getBytes());   //加密后的密文
			//16进制加密
			encode= Hex.encodeHex(bytess);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encode);
	}
	public static void main(String[] args) {
		String aa = "123456";
		String encodingStr = new LoginController().encodePassword(aa);
		System.out.println("encodingStr==="+encodingStr);
		
	}
	
}
