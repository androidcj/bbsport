package com.dawei.vore.service.buyer;

import com.dawei.core.pojo.buyer.BuyerCart;
import com.dawei.core.pojo.order.Order;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.user.Buyer;


public interface BuyerService {
	public Buyer selectUserByUsername(String username);
	public void insertBuyerCarToRedis(BuyerCart buyerCart,String username);
	public BuyerCart getBuyCartFromRedis(String username);
	public Sku getSkuBySkuid(Long skuid);
	
	//保存订单
	public void insertOrder(Order order,String username);
}
