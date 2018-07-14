package com.dawei.vore.service.cms;

import java.util.List;
import java.util.Set;

import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.Sku;

public interface CmsService {
	public Product selectProductById(Long id);
	public List<Sku> selectSkuListByProductId(Long id);
	public Set<Color> queryColors(List<Sku> skus);
}
