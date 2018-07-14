package com.dawei.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dawei.common.utils.RequestUtils;
import com.dawei.vore.service.buyer.SessionProvider;

public class CustomerInterceptor implements HandlerInterceptor{
	
	@Autowired
	public SessionProvider sessionProvider;
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		//判断用户是否登录
		String returnURL = request.getParameter("returnURL");
		String userid = sessionProvider.getAttribute(RequestUtils.getCsessionId(request, response));
		if(userid==null){
			//表示已经登录
			response.sendRedirect("http://localhost:8082/shopping/login.aspx?returnURL="+returnURL);
			return false;
		}
		
		// TODO Auto-generated method stub
		return true;
	}

}
