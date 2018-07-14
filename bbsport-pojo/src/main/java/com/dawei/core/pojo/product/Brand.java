package com.dawei.core.pojo.product;

import java.io.Serializable;

//品牌对象
public class Brand implements Serializable{
	private Long id;
	private String name ;
	private String description;
	private String img_url;
	private String web_site;
	private int sort;
	private int is_display;
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
