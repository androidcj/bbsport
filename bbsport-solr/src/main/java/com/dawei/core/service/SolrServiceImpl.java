package com.dawei.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.page.Pagination;

import com.dawei.core.dao.product.ProductDao;
import com.dawei.core.dao.product.SkuDao;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.ProductQuery;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.product.SkuQuery;
import com.dawei.vore.service.product.BrandService;
import com.dawei.vore.service.product.SolrService;
//solr查询类
@Service("solrService")
public class SolrServiceImpl implements SolrService{
	@Autowired 
	public SolrServer solrServer;
	

	
	//商品检索
	public Pagination selectProductListFromSolr(Integer pageNo,String keyword,Long bId,String pic){
		List<Product> plist=  new ArrayList<Product>();
		
		//设置商品查询对象
		ProductQuery productQuery = new ProductQuery(); 
		productQuery.setPageNo(Pagination.cpn(pageNo));
		productQuery.setPageSize(12);
		SolrQuery solrQuery = new SolrQuery();
		StringBuilder builder = new StringBuilder();
		//设置关键词
		
		//分页
		solrQuery.set("q", "name_ik:"+keyword);
		builder.append("keyword="+keyword);
		
		//排序
		solrQuery.addSort("price",ORDER.asc);
		
		
		//高亮
		//1开启高亮开关
		solrQuery.setHighlight(true);
		//3设置需要高亮的字段
		solrQuery.addHighlightField("name_ik");
		//过滤条件
		if(null !=bId){
			solrQuery.addFilterQuery("brandId:"+bId);
			builder.append("&brandId="+bId);
		}
		
		if(null !=pic){
			String[] bes = pic.split("-");
			if(bes.length==2){
				Float startp = new Float(bes[0]);
				Float endp = new Float(bes[1]);
				solrQuery.addFilterQuery("price:["+startp+" TO "+ endp+ "]");
			}else{
				Float startp = new Float(bes[0]);
				solrQuery.addFilterQuery("price:["+startp+" TO  *]");
			}
			builder.append("&price="+pic);
		}
		
		//设置高亮效果
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		
		
		
		
		solrQuery.setStart(productQuery.getStartRow());  //设置开始行
		solrQuery.setRows(productQuery.getPageSize());
		Pagination pagination = null;
		try {
			 
			QueryResponse response =   solrServer.query(solrQuery);
//			SolrDocumentList  sd = response.getResults();
//			SolrDocument doc = sd.get(0);
			
			//得到返回的高亮
			//
			Map<String,Map<String,List<String>>> highlightmap = response.getHighlighting();
			//第一个key为商品ID   第二个key是商品属性的值 （imgUrl,name_ik....）  list中
			
			
			//得到solr结果集
			SolrDocumentList docs = response.getResults();
			int totalNum =	(int) docs.getNumFound();
			
			pagination = new Pagination(productQuery.getPageNo(),productQuery.getPageSize(),totalNum);
			for (SolrDocument solrDocument : docs) {
				Product product = new Product();
				//ID
				String id  = (String)solrDocument.get("id");
				product.setId(Long.parseLong(id));
				
				Map<String,List<String>> map  = highlightmap.get(id);
				List<String> list = map.get("name_ik");
				String name = "";
				if(list!=null && list.size()>0){
					name = list.get(0);
					System.out.println("高亮内容===="+name);
				}
				//名称
//				String name = (String) solrDocument.get("name_ik");
				product.setName(name);
				//url
				String url = (String) solrDocument.get("url");
				product.setImgUrl(url);
				
				//价格
				Float price = (Float) solrDocument.get("price");
				product.setPrice(price);
				
				//设置品牌ID
				Integer brandId = (Integer) solrDocument.get("brandId");
				product.setBrandId(Long.valueOf(brandId));
				
				plist.add(product);
				
			}
			pagination.setList(plist);
			String uul = "/product/list";
			pagination.pageView(uul, builder.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//过滤条件
		//分页
		//排序
		//高亮
		
		return pagination;
	}
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
