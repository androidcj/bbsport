package buyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.dawei.common.web.Constants;
import com.dawei.vore.service.buyer.SessionProvider;
@Service("sessionProvider")
public class SessionProviderImpl implements SessionProvider,Constants{
	@Autowired
	private Jedis jedis;
	private Integer exp = 30;
	
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	@Override
	public void setAttribute(String key, String value) {
		// TODO Auto-generated method stub
		jedis.set(key+":"+Constants.BUYER_SESSION, value);
		
		//设置时间
		jedis.expire(key+":"+Constants.BUYER_SESSION, 60*exp);
		
	}

	@Override
	public String getAttribute(String key) {
		// TODO Auto-generated method stub
		String value = jedis.get(key+":"+Constants.BUYER_SESSION);
		if(value!=null){
			//设置时间
			jedis.expire(key+":"+Constants.BUYER_SESSION, 60*exp);
			return value;
		}
		return null;
	}
	
	
	//退出
	public void logout(String key){
		jedis.del(key+":"+Constants.BUYER_SESSION);
	}
	
	
}
