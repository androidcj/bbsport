package com.dawei.core.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import cn.itcast.common.page.Pagination;

import com.dawei.core.dao.product.BrandDao;
import com.dawei.core.pojo.product.Brand;
import com.dawei.core.pojo.product.BrandQuery;
import com.dawei.vore.service.product.BrandService;


@Service("brandService")
@Transactional
public class IBrandService implements BrandService{
	@Autowired
	private BrandDao bd;
	@Autowired 
	private Jedis jedis;
	
	//查询redis返回品牌结果集
	
	public List<Brand>  QueryBrandFromRedis(){
		List<Brand> brands = new ArrayList<Brand>();
		Map<String,String> map =jedis.hgetAll("brand");
		Set<Entry<String,String>> entryset = map.entrySet();
		for (Entry<String, String> entry : entryset) {
			Brand brand = new Brand();
			
			String id = entry.getKey();
			String name = entry.getValue();
			brand.setId(Long.parseLong(id));
			brand.setName(name);
			brands.add(brand);
		}
		return brands;
	}
	
	public List<Brand> selectBrandListByQuery(String name,Integer isDisplay){
		List<Brand> list = new ArrayList<Brand>();
		BrandQuery bq = new BrandQuery();
		System.out.println("isDisplay==="+isDisplay);
		System.out.println("name==="+name);
		if(name!=null){
			bq.setName(name);
		}
		if(isDisplay!=null){
			bq.setIs_display(isDisplay);
		}else{
			bq.setIs_display(1);   //默认表示为下架
		}
		list=bd.selectBrandListByQuery(bq);
		return list;
	}
	
	
	//返回分页对象
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Integer isDisplay){
		BrandQuery bq = new BrandQuery();
		StringBuilder params = new StringBuilder();
		
		System.out.println("isDisplay==="+isDisplay);
		System.out.println("name==="+name);
		if(name!=null){
			bq.setName(name);
			params.append("name="+name);
		}
		if(isDisplay!=null){
			bq.setIs_display(isDisplay);
			params.append("&isDisplay="+isDisplay);
		}else{
			bq.setIs_display(1);   //默认表示为下架
			params.append("&isDisplay=1");
		}
		
		//设置pagrSize
		bq.setPageSize(3);
		bq.setPageNo(Pagination.cpn(pageNo));  //如果小于0  或等于0  或是null  改为1
		
		
		Integer allcount = bd.selectCount(bq);
		Pagination pagination = new Pagination(bq.getPageNo(),bq.getPageSize(),allcount);
		//添加结果集
		List<Brand> list = new ArrayList<Brand>();
		bq.setPageNo(pagination.getPageNo());
		list=bd.selectBrandListByQuery(bq);
		pagination.setList(list);
		
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		
		
		return pagination;
		
	}


	
	@Override
	public Brand selectBrandById(Integer id) {
		// TODO Auto-generated method stub
		Brand brand = bd.selectBrandById(id);
		return brand;
	}


	@Override
	public void updateBrand(Brand brand) {
		// TODO Auto-generated method stub
		//保存信息到redis
		bd.updateBrand(brand);
		//保存品牌到redis中
//		jedis.hmset(key, hash)
		jedis.hset("brand", brand.getId()+"", brand.getName());
	}


	@Override
	public void deleteBrand(long[] ids) {
		// TODO Auto-generated method stub
		bd.deleteBrand(ids);
	}
}
