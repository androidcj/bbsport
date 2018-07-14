package com.dawei;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class SolrJTest {
	@Test
	public  void testsolr() throws SolrServerException, IOException{
//		Jedis jedis = new Jedis("192.168.48.129",6379);
//		jedis.set("name", "faceman");
		String path = "http://192.168.48.129:8080/solr/";   //配置solr地址
		SolrServer solrServer = new HttpSolrServer(path);
		SolrInputDocument doc = new SolrInputDocument();
		//保存
		
		doc.setField("id", 2);
		doc.setField("name", "冰冰");
		
		solrServer.add(doc);
		solrServer.commit();
	}
}
