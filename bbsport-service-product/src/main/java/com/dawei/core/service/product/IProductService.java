package com.dawei.core.service.product;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import cn.itcast.common.page.Pagination;

import com.dawei.core.dao.product.ProductDao;
import com.dawei.core.dao.product.SkuDao;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.ProductQuery;
import com.dawei.core.pojo.product.ProductQuery.Criteria;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.product.SkuQuery;
import com.dawei.vore.service.product.ProductService;

@Service("productService")
@Transactional
public class IProductService implements ProductService{
	@Autowired
	private ProductDao pd;
	
	@Autowired
	private SkuDao sd;
	public List<Product> selectByExample(ProductQuery example){
		List<Product> list=	pd.selectByExample(example);
		return list;
	}
	
	
	public Pagination selectPaginationByQuery(Integer pageNo ,String name,Long brandId,Boolean isShow){
		ProductQuery pq = new ProductQuery();
		Criteria criteria = pq.createCriteria();
		StringBuilder params = new StringBuilder();
		if(name!=null){
			criteria.andNameLike("%"+name+"%");
			params.append("name="+name);
		}
		if(null!= brandId){
			criteria.andBrandIdEqualTo(brandId);
			params.append("&brandId="+brandId);
		}
		
		if(null!= isShow){
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow="+isShow);
		}else{
			criteria.andIsShowEqualTo(false);
			params.append("&isShow=false");
		}
		//设置当前页
		
		pq.setPageNo(Pagination.cpn(pageNo));
		pq.setPageSize(3);
		pq.setOrderByClause("id desc");
		Pagination pagination = new Pagination(pq.getPageNo(),pq.getPageSize(),pd.countByExample(pq));
		pq.setPageNo(pagination.getPageNo());
		pagination.setList(pd.selectByExample(pq));
		String url="/product/list.do";
		
		pagination.pageView(url, params.toString());
		return pagination;
	}
	
	@Autowired
	private Jedis jedis;
	
	//添加商品
	public void insertProduct(Product product){
		//得到商品ID
		Long id= jedis.incr("pno");
		product.setId(id);
		
		//设置下架状态
		product.setIsShow(false);
		//设置删除状态为不删除
		product.setIsDel(true);
		//设置时间
		product.setCreateTime(new Date());
		//保存商品
		pd.insertSelective(product);
		
		//保存库存
		String colors[] = product.getColors().split(",");
		
		for(int i=0;i<colors.length;i++){
			String size[] = product.getSizes().split(",");
			for(int j=0;j<size.length;j++){
				Sku sku = new Sku();
				sku.setProductId(product.getId());  //此id为插入之后返回来的id
				sku.setColorId(Long.valueOf(colors[i]));
				sku.setSize(size[j]);
				//市场价、
				sku.setMarketPrice(0f);
				
				//售价
				sku.setPrice(0f);
				//运费
				sku.setDeliveFee(10f);
				//设置库存数
				sku.setStock(0);
				//购买限制
				sku.setUpperLimit(200);
				//保存时间
				sku.setCreateTime(new Date());
				sd.insertSelective(sku);
			}
		}
		
	}
	
	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	//上架
	@Override
	public void isShowProduct(Long[] ids) {
		// TODO Auto-generated method stub
		try {
			Product product = new Product();
			product.setIsShow(true);
			for(int i=0;i<ids.length;i++){
				final Long id = ids[i];
				product.setId(id);
				pd.updateByPrimaryKeySelective(product);
				
				//发送消息到MQ
				jmsTemplate.send(new MessageCreator(){

					@Override
					public Message createMessage(Session session)
							throws JMSException {
						// TODO Auto-generated method stub
						return session.createTextMessage(id+"");
					}
				});
				
				
				/**
				 * //上架之后 保存商品信息到solr服务器
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
				 * 
				 * 
				 */
				
			
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	
		
	}
	
	
}
