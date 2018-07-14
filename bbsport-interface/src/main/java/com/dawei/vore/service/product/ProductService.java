package com.dawei.vore.service.product;

import com.dawei.core.pojo.product.Product;

import cn.itcast.common.page.Pagination;

public interface ProductService {
	public Pagination selectPaginationByQuery(Integer pageNo ,String name,Long brandId,Boolean isShow);
	public void insertProduct(Product product);
	public void isShowProduct(Long ids[]);
	
}
