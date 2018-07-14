package com.dawei.core.pojo.buyer;

import java.io.Serializable;

import com.dawei.core.pojo.product.Sku;

//购物项
public class BuyerItem implements Serializable{
	private Sku sku;
	
	//购买数量
	private Integer amount = 1;
	
	
	//是否有货
	private Boolean ishave = true;


	public Sku getSku() {
		return sku;
	}


	public void setSku(Sku sku) {
		this.sku = sku;
	}


	public Integer getAmount() {
		return amount;
	}


	public void setAmount(Integer amount) {
		this.amount = amount;
	}


	public Boolean getIshave() {
		return ishave;
	}


	public void setIshave(Boolean ishave) {
		this.ishave = ishave;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sku == null) ? 0 : sku.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyerItem other = (BuyerItem) obj;
		if (sku == null) {
			if (other.sku != null)
				return false;
		} else if (!sku.getId().equals(other.sku.getId()))
			return false;
		return true;
	}  
	
	
	
	
}
