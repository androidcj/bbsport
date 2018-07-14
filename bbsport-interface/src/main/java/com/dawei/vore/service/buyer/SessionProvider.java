package com.dawei.vore.service.buyer;

public interface SessionProvider {
	
	//保存用户名密码到redis
	public void setAttribute(String key,String value);
	
	public String getAttribute(String key);
	public void logout(String key);
}
