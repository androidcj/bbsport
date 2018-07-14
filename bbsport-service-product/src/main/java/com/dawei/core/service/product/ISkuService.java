package com.dawei.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawei.core.dao.product.ColorDao;
import com.dawei.core.dao.product.SkuDao;
import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.product.SkuQuery;
import com.dawei.core.pojo.product.SkuQuery.Criteria;
import com.dawei.vore.service.product.SkuService;

@Service("skuService")
@Transactional
public class ISkuService implements SkuService{
	@Autowired
	private SkuDao sd;
	@Autowired
	private ColorDao cd;
	public List<Sku> getSkuList(Long productId){
		SkuQuery sq = new SkuQuery();
	    Criteria criteria =	sq.createCriteria();
	    criteria.andProductIdEqualTo(productId);
		List<Sku> ls =sd.selectByExample(sq);
		for (Sku sku : ls) {
			Long colorid = sku.getColorId();
			Color color = cd.selectByPrimaryKey(colorid);
			sku.setColor(color);
		}
		return ls;
	}
	@Override
	public void updatesku(Sku sku) {
		// TODO Auto-generated method stub
		sd.updateByPrimaryKeySelective(sku);
	}
}
