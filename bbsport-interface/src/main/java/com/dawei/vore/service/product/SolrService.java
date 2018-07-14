package com.dawei.vore.service.product;

import cn.itcast.common.page.Pagination;

//solr搜索接口
public interface SolrService {
	public Pagination selectProductListFromSolr(Integer pageNo,String keyword,Long bId,String pic);
	public void insertProduct(Long id);
	
}
