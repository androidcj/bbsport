package com.dawei.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.dawei.vore.service.cms.StaticPageService;

import freemarker.core.Configurable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 静态化服务类
 * @author win7
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{
	//声明
	private Configuration conf;
	
	private ServletContext servletContext;
	
//	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) throws IOException, TemplateException {
//		this.freeMarkerConfigurer = freeMarkerConfigurer;
		this.conf = freeMarkerConfigurer.createConfiguration();
	}


	public String getPath(String name){
		return servletContext.getRealPath(name);
	}
	
	
	public void index(Long productId,Map<String,Object> root){
		OutputStreamWriter out = null;
		try {
			String path = getPath("/html/product/"+productId+".html");
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			
			
			Template template = conf.getTemplate("productDetail.html");
			out = new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8");
			
			template.process(root, out);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		
		
	}



	@Override
	public void setServletContext(ServletContext sc) {
		// TODO Auto-generated method stub
		servletContext = sc;
	}



	
	
}
