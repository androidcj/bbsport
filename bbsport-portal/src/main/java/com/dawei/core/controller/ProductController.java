package com.dawei.core.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;

import com.dawei.common.utils.RequestUtils;
import com.dawei.common.web.Constants;
import com.dawei.core.pojo.buyer.BuyerCart;
import com.dawei.core.pojo.buyer.BuyerItem;
import com.dawei.core.pojo.product.Brand;
import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.user.Buyer;
import com.dawei.vore.service.buyer.BuyerService;
import com.dawei.vore.service.buyer.SessionProvider;
import com.dawei.vore.service.cms.CmsService;
import com.dawei.vore.service.product.BrandService;
import com.dawei.vore.service.product.SolrService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//前台商品检索
@Controller
public class ProductController {
	@Autowired
	private SolrService solrService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private BuyerService buyerService;
	
	@RequestMapping(value="/product/list")
	public String list(Integer pageNo,String keyword,Long brandId,String price,Model model){
		
		System.out.println("keyword===="+keyword);
		
		Map<String,String> map = new HashMap<String,String>();

		
		Pagination pagination=  solrService.selectProductListFromSolr(pageNo, keyword,brandId,price);
		List<Brand> brands=brandService.QueryBrandFromRedis();
		
		if(null != brandId){
			for (Brand brand : brands) {
				if(brandId == brand.getId()){
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		//价格
		if(null != price){
			String pisc[] = price.split("-");
			if(pisc.length==2){
				map.put("价格", price);
			}else{
				map.put("价格", price+"以上");
			}
			
			
			
		}
		
		
		model.addAttribute("brands", brands);
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		model.addAttribute("map", map);
		model.addAttribute("pagination", pagination);
		return "product";
	}
	
	//去商品详情页
	@Autowired
	private CmsService cmsService;
	
	@RequestMapping(value="/product/detail")
	public String detail(Long id,Model model){
		//商品
		
		Product product = cmsService.selectProductById(id);
		
		model.addAttribute("product", product);
		//库存
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		model.addAttribute("skus", skus);
		
		Set<Color> colors = new HashSet<Color>();
		for(Sku sku:skus){
			Color cl = sku.getColor();
			colors.add(cl);
		}
		model.addAttribute("colors", colors);
		return "productDetail";
		
	}
	
	
	@Autowired
	private SessionProvider sessionProvider ;
	
	//添加商品到购物车中  并保存
	@RequestMapping(value="/shopping/buyerCart")
	public String buyerCart(Long skuId,Integer amount,HttpServletRequest request,HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		//获取cookie中的购物车
		Cookie []cookies = request.getCookies();
		
		ObjectMapper om = new ObjectMapper();
		BuyerCart buyerCart =null;
		om.setSerializationInclusion(Include.NON_NULL);
		if(cookies!=null && cookies.length>0){
			for(int i=0;i<cookies.length;i++){
				Cookie cook = cookies[i];
				String cname = cook.getName();
				
				if(Constants.BUYER_CART.equals(cname)){
					//表示cookie中包含  购物车
					String cvalue = cook.getValue();
					//将字符串转化成对象
					try {
						buyerCart=om.readValue(cvalue, BuyerCart.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}
		
		//判断是否存在购物车
		if(buyerCart ==null){
			buyerCart = new BuyerCart();
			
		}
		
		
		//追加商品到购物车
		
		
		if(skuId !=null){
			//如果库存ID不为空
			Sku sku = new Sku();
			sku.setId(skuId);
			BuyerItem bitem = new BuyerItem();
			bitem.setSku(sku);
			bitem.setAmount(amount);
			buyerCart.addItem(bitem);
		}
		
		
		//判断用户是否登录
		
		String username = sessionProvider.getAttribute(RequestUtils.getCsessionId(request, response));
		if(username !=null ){
			//登录了
			//把购物车追加到redis中
			buyerService.insertBuyerCarToRedis(buyerCart, username);
			//清空cookie
			Cookie cookie = new Cookie(Constants.BUYER_CART,null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			
			
		}else{
			//非登录
			StringWriter w = new StringWriter();
			om.writeValue(w, buyerCart);
			Cookie cookie = new Cookie(Constants.BUYER_CART,w.toString());
			cookie.setPath("/");  //设置路径
			cookie.setMaxAge(60*60*24);  //设置存活时间1天
			
			//写回浏览器
			response.addCookie(cookie);
			
		}
		return "redirect:/shopping/toCart";
		
	}
	
	
	//进入到购物车页面
	@RequestMapping(value="/shopping/toCart")
	public String toCart(Long skuId,Integer amount,HttpServletRequest request,HttpServletResponse response,Model model) throws JsonParseException, JsonMappingException, IOException{
		//1判断用户知否登录
		String username = sessionProvider.getAttribute(RequestUtils.getCsessionId(request, response));
		BuyerCart buyerCart = null;
		
		Cookie cookies[] =  request.getCookies();
		if(cookies!=null){
				
			for(int i=0;i<cookies.length;i++){
				Cookie cook = cookies[i];
				String cname = cook.getName();
				if(Constants.BUYER_CART.equals(cname)){
					//表示存在购车
					String cartStr = cook.getValue();
					ObjectMapper om = new ObjectMapper();
					om.setSerializationInclusion(Include.NON_NULL);
					buyerCart = om.readValue(cartStr, BuyerCart.class);
				}
			}
		
			
		}
		

		
		if(username==null){
			//表示没有登录
			if(buyerCart==null){
				buyerCart = new BuyerCart();
			}
			
			
		}else{
			//表示登录
			//获取redis中的购物车
			//把cookie中的购物车  保存到redis中
			
			buyerService.insertBuyerCarToRedis(buyerCart, username);
			//清空购物车 cookie
			Cookie cookie = new Cookie(Constants.BUYER_CART,null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			
			buyerCart = buyerService.getBuyCartFromRedis(username);
		}
		
		
		//给购物车填充数据
		List<BuyerItem> buyerItems = buyerCart.getItems();
		for (BuyerItem buyerItem : buyerItems) {
			Long skuid = buyerItem.getSku().getId();
			Sku sku = buyerService.getSkuBySkuid(skuid);
			buyerItem.setSku(sku);
		}
		//回显到购物车页面
		model.addAttribute("buyerCart", buyerCart);
		return "cart";
	}
	
}
