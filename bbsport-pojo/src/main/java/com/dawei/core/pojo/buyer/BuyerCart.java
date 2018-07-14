package com.dawei.core.pojo.buyer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

//购物车对象
public class BuyerCart implements Serializable{

	//结果集
	private List<BuyerItem> items = new ArrayList<BuyerItem>();

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}
	
	//添加商品到购物车
	public void addItem(BuyerItem bitem){
		if(items.contains(bitem)){
			
			for (BuyerItem buyerItem : items) {
				if(buyerItem.equals(bitem) ){
					//设置数量
					buyerItem.setAmount(buyerItem.getAmount()+bitem.getAmount());
				}
			}
			
		}else{
			this.items.add(bitem);
		}
		
		
	}
	
	
	
	
	//小计
	//得到商品数量
	@JsonIgnore
	public Integer getProductAmount(){
		Integer result = 0;
		for (BuyerItem buyerItem : items) {
			result = result+buyerItem.getAmount();
		}
		return result;
		
	}
	//商品金额
	@JsonIgnore
	public Float getProductPrice(){
		Float result = 0f;
		
		for (BuyerItem buyerItem : items) {
			result+=buyerItem.getAmount()*buyerItem.getSku().getPrice();
		}
		return result;
	}
	
	//运费
	@JsonIgnore
	public Float getFee(){
		Float result = 0f;
		if(getProductPrice()<79){
			result = 5f;
		}
		return result;
	}
	
	//应付金额
	@JsonIgnore
	public Float getTotalPrice(){
		return getProductPrice()+getFee();
	}
	
		
	
}
