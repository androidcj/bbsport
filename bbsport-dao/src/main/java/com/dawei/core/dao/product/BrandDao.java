package com.dawei.core.dao.product;

import java.util.List;

import com.dawei.core.pojo.product.Brand;
import com.dawei.core.pojo.product.BrandQuery;

public interface BrandDao {
	public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
	//查询符合条件的总条数
	public Integer selectCount(BrandQuery brandQuery);
	
	//根据id查询品牌
	public Brand selectBrandById(Integer id);
	
	//品牌修改
	public void updateBrand(Brand brand);
	//品牌删除
	public void deleteBrand(long[]ids);
	
}	
