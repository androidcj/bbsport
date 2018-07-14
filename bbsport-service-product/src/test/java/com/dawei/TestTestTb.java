package com.dawei;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dawei.core.dao.TestTBDao;
import com.dawei.core.pojo.TestDB;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestTestTb {
	@Autowired
	private TestTBDao testTbDao;
	
	@Test
	public void testName() throws Exception {
		TestDB testdb = new TestDB();
		testdb.setName("刘德华");
		testdb.setBirthday(new Date());
		testTbDao.insertTestDb(testdb);
	}
}
