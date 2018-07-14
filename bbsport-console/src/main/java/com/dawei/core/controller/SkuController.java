package com.dawei.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dawei.core.pojo.product.Sku;
import com.dawei.core.pojo.product.SkuQuery;
import com.dawei.core.pojo.product.SkuQuery.Criteria;
import com.dawei.vore.service.product.SkuService;
//库存controller

@Controller
public class SkuController {
	
	@Autowired
	private SkuService skuService;
	
@RequestMapping(value="/sku/list.do")
	public String skulist(Long productId,Model model){
	
		List<Sku> skus = new ArrayList<Sku>();
		skus = skuService.getSkuList(productId);
		System.out.println("skus===="+skus.size());
		
		model.addAttribute("skus",skus);
	//返回到库存的list页面
		return "sku/list";
	}

	@RequestMapping(value="/sku/update.do")
	public void updatesku(Sku sku,HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		
		try {
			skuService.updatesku(sku);
			json.put("message", "保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			json.put("message", "保存失败");
			e.printStackTrace();
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json.toString());
	}

}
