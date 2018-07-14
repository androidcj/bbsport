package com.dawei.core.service;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.dawei.core.dao.product.ProductDao;
import com.dawei.core.dao.product.SkuDao;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.product.SkuQuery;

public class MQServiceImpl {
	
	
	//保存商品信息到solr服务器
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private ProductDao pd;
	@Autowired
	private SkuDao sd;
	public void insertProduct(Long id){
		
		
		try {
			//上架之后 保存商品信息到solr服务器
			SolrInputDocument doc = new SolrInputDocument();
			//id 图片  
			
			//设置商品ID
			doc.setField("id", id);
			
			//设置商品名称
			Product p =pd.selectByPrimaryKey(id);
			doc.setField("name_ik", p.getName());   //name设置为ik分词器字段
			
			//设置照片的url
			doc.setField("url", p.getImages() == null?"":p.getImages()[0]);
			//商品售价
			SkuQuery sku = new SkuQuery();
			sku.setFields("price");
			sku.createCriteria().andProductIdEqualTo(id);
			sku.setOrderByClause(" price asc");
			sku.setPageNo(1);
			sku.setPageSize(1);
			List<Sku> skus=sd.selectByExample(sku);
			//设置最小的价格  在前台页面展现
			if(skus!=null && skus.size()>0){
				doc.setField("price", skus.get(0).getPrice()); 
			}else{
				doc.setField("price",0); 
			}
			//品牌ID
			doc.setField("brandId",p.getBrandId()); 
			solrServer.add(doc);
			solrServer.commit();
			//静态化
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
