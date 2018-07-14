package buyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.dawei.common.web.Constants;
import com.dawei.core.dao.order.DetailDao;
import com.dawei.core.dao.order.OrderDao;
import com.dawei.core.dao.product.ColorDao;
import com.dawei.core.dao.product.ProductDao;
import com.dawei.core.dao.product.SkuDao;
import com.dawei.core.dao.user.BuyerDao;
import com.dawei.core.pojo.buyer.BuyerCart;
import com.dawei.core.pojo.buyer.BuyerItem;
import com.dawei.core.pojo.order.Detail;
import com.dawei.core.pojo.order.Order;
import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.user.Buyer;
import com.dawei.core.pojo.user.BuyerQuery;
import com.dawei.vore.service.buyer.BuyerService;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{
	//根据用户名 查询用户
	
	@Autowired
	private BuyerDao buyerDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ColorDao colorDao;
	
	@Autowired
	private OrderDao orderDao;
	
	//订单详情DAO
	@Autowired
	private DetailDao detailDao;
	
	
	
	@Autowired
	private Jedis jedis;
	
	public Buyer selectUserByUsername(String username){
		BuyerQuery bq = new BuyerQuery();
		bq.createCriteria().andUsernameEqualTo(username);
		
		
		List<Buyer> blist =  buyerDao.selectByExample(bq);
		if(blist !=null && blist.size()>0){
			return  blist.get(0);
		}
		return null;
	}
	
	
	
	//存放购物车到redis中
	public void insertBuyerCarToRedis(BuyerCart buyerCart,String username){
//		jedis.hset(Constants.BUYER_CART, buyerCar, value);
		if(buyerCart!=null){
			List<BuyerItem> items = buyerCart.getItems();
			for (BuyerItem buyerItem : items) {
				
				//判断redis中的商品是否已经存在
				Long skuid = buyerItem.getSku().getId();
				Integer amount =  buyerItem.getAmount();
				boolean res=  jedis.hexists("buyerCart:"+username, skuid+"");
				if(res){
					//存在
//					Map<String,String> map = jedis.hgetAll("buyerCart:"+username);
//					map.put(skuid+"", map.get(skuid+"")+amount);
					jedis.hincrBy("buyerCart:"+username,skuid+"", amount);
					
				}else{
					jedis.hset("buyerCart:"+username, skuid+"", amount+"");
				}
				
			}
		}
		
	}
	
	//获取购物车 从redis
	
	public BuyerCart getBuyCartFromRedis(String username){
		BuyerCart buyerCart = new BuyerCart();
		Map<String,String> map  =jedis.hgetAll("buyerCart:"+username);
		if(map !=null){
			Set<Entry<String,String>> entryset = map.entrySet();
			List<BuyerItem> items = new ArrayList<BuyerItem>();
			for (Entry<String, String> entry : entryset) {
				BuyerItem buyerItem = new BuyerItem();
				Sku sku = new Sku();
				String skuid = entry.getKey();
				sku.setId(Long.valueOf(skuid));
				buyerItem.setSku(sku);
				String amont = entry.getValue();
				buyerItem.setAmount(Integer.parseInt(amont));
				items.add(buyerItem);
			}
			buyerCart.setItems(items);
		}
	
		return buyerCart;
	}
	
	//根据skuid查询sku对象
	public Sku getSkuBySkuid(Long skuid){
		//得到sku对象
		Sku sku = skuDao.selectByPrimaryKey(skuid);  
		
//		sku.getProductId()
//		sku.getColorId()
		//得到商品对象
		Product product = productDao.selectByPrimaryKey(sku.getProductId());
		//得到颜色对象
		Color color = colorDao.selectByPrimaryKey(sku.getColorId());
		sku.setColor(color);
		sku.setProduct(product);
		return sku;
	}
	
	
	//保存订单
	public void insertOrder(Order order,String username){
		long orderid = jedis.incrBy(Constants.ORDER_ID, 1);   //得到orderID
		order.setId(orderid);
		BuyerCart buyerCart = getBuyCartFromRedis(username);
		List<BuyerItem>items  =  buyerCart.getItems();
		for (BuyerItem buyerItem : items) {
			Sku sku = getSkuBySkuid(buyerItem.getSku().getId());
			buyerItem.setSku(sku);
		}
		//运费
		order.setDeliverFee(buyerCart.getFee());
		
		//订单总金额
		order.setTotalPrice(buyerCart.getTotalPrice());
		
		
		//商品金额
		order.setOrderPrice(buyerCart.getProductPrice());
		
		//支付状态   0到付     1代付款  2  已付款  3 带退款  4  退款成功  5 退款失败 
		if(order.getPaymentWay()==0){
			//如果是货到付款
			order.setIsPaiy(0);
		}else{
			order.setIsPaiy(1);
		}
		
		
		//订单状态  0提交订单  1 仓库配货  2 商品出库  3 等待收货  4  完成  5  待退货  6  已退货

		order.setOrderState(0);
		
		
		//保存时间
		order.setCreateDate(new Date());
		
		//用户ID    按道理来说 这个应该通过redis来获取   redis中  K:V    为用户名：用户ID    当用户注册时   存入redis以及mysql
		order.setBuyerId(1l);
		
		
		
		//保存 订单明细  
		orderDao.insertSelective(order);
		
		
		//保存订单详情
		for (BuyerItem buyerItem : buyerCart.getItems()) {
			Detail detail = new Detail();
			//订单ID
			detail.setOrderId(orderid);
			//设置商品ID编号
			detail.setProductId(buyerItem.getSku().getProduct().getId());
			
			//商品名称
			detail.setProductName(buyerItem.getSku().getProduct().getName());
			
			//商品颜色
			detail.setColor(buyerItem.getSku().getColor().getName());
			
			//设置尺码
			detail.setSize(buyerItem.getSku().getSize());
			
			//设置价格
			detail.setPrice(buyerItem.getSku().getPrice());
			
			//购买数量
			detail.setAmount(buyerItem.getAmount());
			detailDao.insertSelective(detail);
		}
		//清理redis中的购物车
		jedis.del("buyerCart:"+username);
	}
	
}
