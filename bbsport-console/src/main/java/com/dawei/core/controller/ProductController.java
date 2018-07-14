package com.dawei.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;

import com.dawei.core.pojo.product.Brand;
import com.dawei.core.pojo.product.Color;
import com.dawei.core.pojo.product.Product;
import com.dawei.vore.service.product.BrandService;
import com.dawei.vore.service.product.ColorService;
import com.dawei.vore.service.product.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	ColorService colorService;
	@Autowired
	BrandService brandService;
	
	@RequestMapping(value="/product/list.do")
	public String productList(Integer pageNo ,String name,Long brandId,Boolean isShow,Model model){
		List<Brand> brands=  brandService.selectBrandListByQuery(null, 1);
		model.addAttribute("brands", brands);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("isShow", isShow);
		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		model.addAttribute("pagination", pagination);
		return "product/list";
	}
	
	
	@RequestMapping(value="/product/toAdd.do")
	public String product_add(Long brandId ,String name,Long typeId,Long weight,String colors,String sizes,Model model){
		List<Brand> brands=  brandService.selectBrandListByQuery(null, 1);
		System.out.println("brands==="+brands.size());
		
		List<Color> colorlisr = colorService.colorList();
		
		model.addAttribute("brands", brands);
		model.addAttribute("colors", colorlisr);
//		model.addAttribute("name", name);
//		model.addAttribute("brandId", brandId);
//		model.addAttribute("isShow", isShow);
//		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
//		model.addAttribute("pagination", pagination);
		return "product/add";
	}
	
	@RequestMapping(value="/product/add.do")
	public String productadd(Product product){
		
		//保存商品信息
		productService.insertProduct(product);
		//保存库存信息
		return "redirect:/product/list.do";
	}
	
	@RequestMapping(value="/product/isShow.do")
//	public String productisShow(Long ids[],String name,Long brandId,Integer isShow,Integer pageNo,Model model){
	public String productisShow(Long ids[]){	
//		model.addAttribute("name", name);
//		model.addAttribute("brandId", brandId);
//		model.addAttribute("isShow", isShow);
//		model.addAttribute("pageNo", pageNo);
		
		//保存商品信息
		productService.isShowProduct(ids);
		//保存库存信息
		return "forward:/product/list.do";
	}
}
