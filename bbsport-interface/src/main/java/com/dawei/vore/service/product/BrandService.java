package com.dawei.vore.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;

import com.dawei.core.pojo.buyer.BuyerCart;
import com.dawei.core.pojo.product.Brand;

public interface BrandService {
	public List<Brand> selectBrandListByQuery(String name,Integer isDisplay);
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Integer isDisplay);
	public Brand selectBrandById(Integer id);
	public void updateBrand(Brand brand);
	public void deleteBrand(long []ids);
	//redi去除品牌
	public List<Brand>  QueryBrandFromRedis();
}
