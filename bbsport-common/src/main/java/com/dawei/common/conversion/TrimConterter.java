package com.dawei.common.conversion;

import org.springframework.core.convert.converter.Converter;

//转换器去掉前后空格
public class TrimConterter implements Converter<String, String>{

	@Override
	public String convert(String source) {
		// TODO Auto-generated method stub
		try {
			if(source!=null){
				source = source.trim();
				if(!"".equals(source)){
					return source;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		return null;
	}

}
