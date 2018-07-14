package com.dawei.core.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawei.core.dao.product.ColorDao;
import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.ColorQuery;
import com.dawei.core.pojo.product.ColorQuery.Criteria;
import com.dawei.vore.service.product.ColorService;
@Service("colorService")
@Transactional
public class IColorService implements ColorService{
	@Autowired
	public ColorDao cd;
	
	//颜色结果集
	@RequestMapping(value="/product/colorList")
	public List<Color> colorList(){
		List<Color> clist=  new ArrayList<Color>();
		try {
			ColorQuery cq = new ColorQuery();
//			List<Criteria> oredCriteria =	cq.createCriteria();
//			oredCriteria
			Criteria  criteria =cq.createCriteria();
			criteria.andParentIdNotEqualTo(0L);
			 clist=  cd.selectByExample(cq);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		return clist;
	}
	
	
}
