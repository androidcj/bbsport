package com.dawei;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class RedisTest {
	@Test
	public  void testredis(){
		Jedis jedis = new Jedis("192.168.48.129",6379);
		jedis.set("name", "faceman");
		
	}
}
