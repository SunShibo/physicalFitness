package com.ichzh.physicalFitness.web.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;

public class TranslateServletInputStream{

	public static ByteArrayInputStream stringToByteInputStream(String body) {
		//不编码
		ByteArrayInputStream in_nocode = new ByteArrayInputStream(body.getBytes());
//		try {
//			//编码
//			InputStream   in_withcode   =   new   ByteArrayInputStream(body.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
		return in_nocode;
	}
	
	public static ServletInputStream InputStreamToServletInputStream(ByteArrayInputStream in_nocode) {
		ServletInputStream servletInputStream=new ServletInputStream(){
	        public int read() throws IOException {
	          return in_nocode.read();
	        }
	      };
		return servletInputStream;
	}
}
