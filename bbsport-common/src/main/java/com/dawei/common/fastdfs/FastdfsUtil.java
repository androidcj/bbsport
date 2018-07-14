package com.dawei.common.fastdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

//文件系统工具类
public class FastdfsUtil {
	public static String uploadPic(byte[] pic,String name,long size){
		String path = "";
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		
		try {
			System.out.println("path====="+resource.getClassLoader().getResource("fdfs_client.conf").getPath());
			ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
			TrackerClient tc = new TrackerClient();
			TrackerServer ts =tc.getConnection();
//			StorageServer1 storageClient1 =null;
			StorageServer ss = null;
			StorageClient1 sc1 = new StorageClient1(ts,ss);
			
			//上传图片
			String ext = FilenameUtils.getExtension(name);   //得到拓展名
			NameValuePair nvp[] = new NameValuePair[3];
			nvp[0]=new NameValuePair("filename",name);
			nvp[1]=new NameValuePair("ext",ext);
			nvp[2]=new NameValuePair("size",String.valueOf(size));
			path=sc1.upload_file1(pic, ext, nvp);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//连接tracker跟踪器客户端
		System.out.println("resultPath==="+path);
		return path;
		
	}
}
