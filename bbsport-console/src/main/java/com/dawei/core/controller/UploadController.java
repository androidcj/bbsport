package com.dawei.core.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.dawei.common.web.Constants;
import com.dawei.vore.service.UploadService;

@Controller
public class UploadController {
	@Autowired
	public UploadService uploadService;
	
	
	@RequestMapping(value="/upload/uploadPic.do")
	public void uploadPic(MultipartFile pic,HttpServletRequest request,HttpServletResponse response){
		String path = request.getSession().getServletContext().getRealPath("");
		SimpleDateFormat  sdf =new SimpleDateFormat("yyyyMMddHHmmssSSS");   //精确的毫秒
		String nowtime = sdf.format(new Date());
		String name = nowtime;
		
		//设置随机
		Random rd = new Random();
		for(int i=0;i<3;i++){
			name = name+rd.nextInt(10);
		}
		
		path =path+ File.separator+"upload"+File.separator+name+"."+FilenameUtils.getExtension(pic.getOriginalFilename());
//		pic.transferTo(path);
		
		String showPth = File.separator+"upload"+File.separator+name+"."+FilenameUtils.getExtension(pic.getOriginalFilename());
		
		String file_name = pic.getOriginalFilename();  //得到文件的名字
		
		
		
		System.out.println("path====="+path);
		try {
			InputStream is= pic.getInputStream();
			byte[]bts = new byte[1024];
			FileOutputStream tos = new FileOutputStream(path);
			int filelong = 0;
			while((filelong = is.read(bts))!=-1){
				tos.write(bts);
			}
			
			JSONObject jobject = new JSONObject();
			jobject.put("path", showPth);
			System.out.println("jobject===="+jobject.toString());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jobject.toString());
			
			//上传的分布式文件系统
//			String showPth_dfs = uploadService.uploadPic(pic.getBytes(), file_name, pic.getSize());
//			System.out.println("showPth_dfs====="+showPth_dfs);
//			
//			showPth = Constants.IMG_URL+showPth_dfs;
//			System.out.println("showPth====="+showPth);
			jobject.put("path", showPth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ppp==="+pic.getOriginalFilename());
	}
	
	
	@RequestMapping(value="/upload/uploadPics.do")
	public @ResponseBody List<String> uploadPics(@RequestParam(required=false) MultipartFile[] pics,HttpServletRequest request,HttpServletResponse response){
		//得到路径结果集
		List<String> urls = new ArrayList<String>();
		SimpleDateFormat  sdf =new SimpleDateFormat("yyyyMMddHHmmssSSS");   //精确的毫秒
		
		String path = request.getSession().getServletContext().getRealPath("");
		try {
			for(int i=0;i<pics.length;i++){
				String nowtime = sdf.format(new Date());
				String name = nowtime;
				MultipartFile pic = pics[i];
				urls.add(File.separator+"upload"+File.separator+name+"."+FilenameUtils.getExtension(pic.getOriginalFilename()));
//				System.out.println("url===="+File.separator+"upload"+File.separator+name+"."+FilenameUtils.getExtension(pic.getOriginalFilename()));
				String showpath =path+ File.separator+"upload"+File.separator+name+"."+FilenameUtils.getExtension(pic.getOriginalFilename());
				System.out.println("showpath==="+showpath);
				pic.transferTo(new File(showpath));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return urls;
	}
	
	
	//kindEditor上传
	@RequestMapping(value="/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response){
		try {
			MultipartRequest mr = (MultipartRequest) request;
			Map<String,MultipartFile> mmp = mr.getFileMap();
			Set<String> set= mmp.keySet();
			String path = request.getSession().getServletContext().getRealPath("");
			SimpleDateFormat  sdf =new SimpleDateFormat("yyyyMMddHHmmssSSS");   //精确的毫秒
			for (String string : set) {
				String datename = sdf.format(new Date());
				MultipartFile pic = mmp.get(string);
				String showpath = path+File.separator+"upload"+File.separator+datename+"."+FilenameUtils.getExtension(pic.getOriginalFilename());
				System.out.println("showpath===="+showpath);
				pic.transferTo(new File(showpath));
				//返回路径
				JSONObject json = new JSONObject();
				String url = "http://192.168.48.1:8080/upload/20171028222652890.jpg";
//				json.put("url", File.separator+"upload"+File.separator+datename+"."+FilenameUtils.getExtension(pic.getOriginalFilename()));
//				json.put("url", showpath);
				json.put("url", url);
				json.put("error", 0);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write(json.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		
	}
	
}
