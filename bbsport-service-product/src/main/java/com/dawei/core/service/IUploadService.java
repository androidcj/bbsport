package com.dawei.core.service;

import org.springframework.stereotype.Service;

import com.dawei.common.fastdfs.FastdfsUtil;
import com.dawei.vore.service.UploadService;
@Service("uploadService")
public class IUploadService implements UploadService{

	@Override
	public String uploadPic(byte[] pic, String name, long size) {
		// TODO Auto-generated method stub
		String result = FastdfsUtil.uploadPic(pic, name, size);
		return result;
	}
	
}
