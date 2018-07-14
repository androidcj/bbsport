package com.dawei.vore.service.product;

import java.util.List;

import com.dawei.core.pojo.product.Sku;

public interface SkuService {
	  public List<Sku> getSkuList(Long productId);
	  public void updatesku(Sku sku);
}
