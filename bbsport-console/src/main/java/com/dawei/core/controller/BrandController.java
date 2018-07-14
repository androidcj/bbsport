package com.dawei.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;

import com.dawei.core.pojo.product.Brand;
import com.dawei.vore.service.product.BrandService;
@Controller
//品牌管理控制器
public class BrandController {
	@Autowired
	private BrandService brandService;
	//查询品牌列表
	
	@RequestMapping("/brand/list.do")
	public String list(Integer pageNo,String name,Integer isDisplay,Model model){
		//显示品牌列表
//		List<Brand> brands = brandService.selectBrandListByQuery(name, isDisplay);
//		System.out.println("brandList===="+brands.size());
		
		Pagination pagination = brandService.selectPaginationByQuery(pageNo, name, isDisplay);
		
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		model.addAttribute("pagination", pagination);
//		model.addAttribute("brands", brands);
		return "brand/list";
	}
	
	//修改品牌
	@RequestMapping("/brand/toEdit.do")
	public String toEdit(Integer id,Model model){
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	
	
	//修改品牌
	@RequestMapping("/brand/brandedit.do")
	public String brandedit(Brand brand){
		brandService.updateBrand(brand);
		return "redirect:/brand/list.do";
	}
	
	
	//删除品牌
		@RequestMapping("/brand/branddeletes.do")
		public String branddeletes(long ids[],String name,Integer isDisplay,Integer pageNo,Model model){
			brandService.deleteBrand(ids);
			model.addAttribute("name", name);
			model.addAttribute("isDisplay", isDisplay);
			model.addAttribute("pageNo", pageNo);
			return "redirect:/brand/list.do";
		}
	
}
