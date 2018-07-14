package com.dawei.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawei.core.dao.product.ColorDao;
import com.dawei.core.dao.product.ProductDao;
import com.dawei.core.dao.product.SkuDao;
import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Product;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.product.SkuQuery;
import com.dawei.vore.service.cms.CmsService;

/**
 * 内容管理
 * @author win7
 *
 */

@Service("cmsService")
public class CmsServiceImpl implements CmsService{
	
	@Autowired
	private ProductDao pd;
	@Autowired
	private SkuDao sd;
	
	@Autowired
	private ColorDao cd;
	
	
	//查询商品表
	public Product selectProductById(Long id){
		
		Product product=pd.selectByPrimaryKey(id);
		
		return product;
	}
	
	//查询库存
	public List<Sku> selectSkuListByProductId(Long id){
		SkuQuery suq = new SkuQuery();
		suq.createCriteria().andProductIdEqualTo(id).andStockGreaterThan(0);
		List<Sku> list= sd.selectByExample(suq);
		for (Sku sku : list) {
			sku.setColor(cd.selectByPrimaryKey(sku.getColorId()));
		}
//		Set<Color> colors = new HashSet<Color>();
//		for(Sku sku:list){
//			Color cl = sku.getColor();
//			colors.add(cl);
//		}
		return list;
	}
	
	public Set<Color> queryColors(List<Sku> skus){
		Set<Color> colors = new HashSet<Color>();
		for(Sku sku:skus){
			Color cl = sku.getColor();
			colors.add(cl);
		}
		return colors;
	}
	
}
