package com.dawei.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawei.common.utils.RequestUtils;
import com.dawei.core.pojo.buyer.BuyerCart;
import com.dawei.core.pojo.buyer.BuyerItem;
import com.dawei.core.pojo.order.Order;
import com.dawei.core.pojo.product.Sku;
import com.dawei.vore.service.buyer.BuyerService;
import com.dawei.vore.service.buyer.SessionProvider;
import com.dawei.vore.service.product.SkuService;

@Controller
public class BuyerController {
	
	@Autowired
	private BuyerService buyerService;
	
	@Autowired
	private SessionProvider sessionProvider ;
	
	@RequestMapping(value="/buyer/trueBuy")
	//结算
	public String trueBuy(String returnURL,HttpServletRequest request,HttpServletResponse response,Model model){
		//判断购物车中是否有东西
		
		String username=  sessionProvider.getAttribute(RequestUtils.getCsessionId(request, response));
		BuyerCart buyerCart= buyerService.getBuyCartFromRedis(username);
		List<BuyerItem> items = buyerCart.getItems();
		if(items.size()>0){
			//有商品
			//判断是否有货
			boolean hasStock = true;
			
			for (BuyerItem buyerItem : items) {
				long skuid = buyerItem.getSku().getId();
				//得到库存
				//通过skuid得到  sku  在得到sku中的库存
				Integer amount = buyerItem.getAmount();
				Sku sku = buyerService.getSkuBySkuid(skuid);
				
				//得到库存
				Integer stock = sku.getStock();
				if(stock<amount){
					//表示没货
					buyerItem.setIshave(false);
					hasStock = false;
					
				}
				
			}
			
			if(!hasStock){
				//表示存在无货的情况
				//回显到购物车页面
				model.addAttribute("buyerCart", buyerCart);
				return "cart";
			}
			
		}else{
			//无商品  则去购物车页面
			
			return "redirect:/shopping/toCart";
		}
		
		return "productOrder";
	}

	
	
	//保存订单
	@RequestMapping(value="/buyer/confirmOrder")
	public String confirmOrder(HttpServletRequest request,HttpServletResponse response,Order order,Model model){
		
		
		
		buyerService.insertOrder(order, sessionProvider.getAttribute(RequestUtils.getCsessionId(request, response)));
		return "confirmOrder";
	}
	
	
}
