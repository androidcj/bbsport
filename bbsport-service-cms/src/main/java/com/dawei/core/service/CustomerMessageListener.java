package com.dawei.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.Sku;
import com.dawei.vore.service.cms.CmsService;
import com.dawei.vore.service.cms.StaticPageService;

public class CustomerMessageListener implements MessageListener{
	@Autowired
	private StaticPageService staticPageService;
	
	@Autowired
	private CmsService cmsService;
	
	@Override
	public void onMessage(Message message) {
		
		
		// TODO Auto-generated method stub
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		Map<String,Object> root = new HashMap<String,Object>();
		try {
			System.out.println("cms中得到的信息是===="+am.getText());
			String productId = am.getText();
			Product product  = cmsService.selectProductById(Long.valueOf(productId));
			root.put("product", product);
			List<Sku> skus = cmsService.selectSkuListByProductId(Long.valueOf(productId));
			root.put("skus", skus);
			Set<Color> colors = cmsService.queryColors(skus);
			root.put("colors", colors);
			
			staticPageService.index(Long.valueOf(productId), root);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
