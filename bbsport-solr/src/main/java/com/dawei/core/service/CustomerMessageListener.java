package com.dawei.core.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.dawei.vore.service.product.SolrService;

public class CustomerMessageListener implements MessageListener{
	@Autowired
	private SolrService solrService;
	
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		try {
			System.out.println("得到的信息是===="+am.getText());
			solrService.insertProduct(Long.valueOf(am.getText()));
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
