package com.dawei.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawei.core.dao.TestTBDao;
import com.dawei.core.pojo.TestDB;
import com.dawei.vore.service.TestTbService;

/*测试事物
 * 
 * */

@Service("testTbService")
@Transactional
public class ITestTbService implements TestTbService {
	@Autowired 
	private TestTBDao tt;
	
	
	@Override
	public void insertTestDb(TestDB testdb) {
		// TODO Auto-generated method stub
		tt.insertTestDb(testdb);
	}
}
