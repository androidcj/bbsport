package com.dawei.core.pojo.product;

import java.io.Serializable;

//品牌对象 vo类
public class BrandQuery implements Serializable{
	private Long id;
	private String name ;
	private String description;
	private String img_url;
	private String web_site;
	private int sort;
	private int is_display;
	
	
	//分页信息
	private int pageNo =1;   //当前页
	private int pageSize =30;   //每页数
	private int startRow =0;   //开始行
	
	
	
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.startRow = (pageNo-1)*pageSize;
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.startRow = (pageNo-1)*pageSize;
		this.pageSize = pageSize;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getWeb_site() {
		return web_site;
	}
	public void setWeb_site(String web_site) {
		this.web_site = web_site;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getIs_display() {
		return is_display;
	}
	public void setIs_display(int is_display) {
		this.is_display = is_display;
	}
}
