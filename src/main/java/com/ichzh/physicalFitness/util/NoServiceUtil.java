package com.ichzh.physicalFitness.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoServiceUtil {
	
	/**
	 * Float计算类型——加法
	 */
	public static String FLOAT_CAL_TYPE_ADD = "add";
	/**
	 * Float计算类型——减法
	 */
	public static String FLOAT_CAL_TYPE_SUBTRACT = "subtract";
	/**
	 * Float计算类型——乘法
	 */
	public static String FLOAT_CAL_TYPE_MULTIPLY = "multiply";
	/**
	 * Float计算类型——除法
	 */
	public static String FLOAT_CAL_TYPE_DIVIDE = "divide";
	
	/**
	 * 将日期转换为年月日时分秒的字符串
	 * @param date
	 * @return
	 */
	public static String dateTimeToString(Date date){
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return time.format(date);
	}
	
	//上传附件
	public static boolean saveFile(MultipartFile file,String filename,String path){
		boolean isOk = false;
		try {
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			file.transferTo(new File(path, filename));
			isOk = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 随机获取UUID
	 * @return lkjlwkejrlwkj3o2i3u4975879
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 获取文件格式
	 * @param fileItem
	 * @return “.doc”、“.xls”
	 */
	public static String getFileFormat(MultipartFile fileItem){
		String flag="";
		int filePoint = fileItem.getOriginalFilename().lastIndexOf(".");
		flag = fileItem.getOriginalFilename().substring(filePoint);
		return flag;
	}
	
	/**
	 * 格式化HTML文本
	 * @param content
	 * @return
	 */
	public static String html(String content) {
		if(content==null) return "";
	    String html = content;
//	    html = StringUtils.replace(html, "&apos;","'");
//	    html = StringUtils.replace(html, "&quot;","\"");
//	    html = StringUtils.replace(html, "&nbsp;&nbsp;","\t");// 替换跳格
//	    html = StringUtils.replace(html, "&nbsp;"," ");// 替换空格
//	    html = StringUtils.replace(html, "&lt;","<");
//	    html = StringUtils.replace(html, "&gt;",">");
//	    html = StringUtils.replace(html, "&amp;","&");
//	    html = StringUtils.replace(html, "&times;","?á");
//	    html = StringUtils.replace(html, "&divide;","??");
//	    html = StringUtils.replace(html, "&ensp;","         ");
//	    html = StringUtils.replace(html, "&emsp;","         ");
	    return html;
	}
	/**
	 * 格式化HTML文本
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    }
	
	/**
	 * 格式化HTML文本
	 * @param htmlStr
	 * @return
	 */
	public static String money(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    }
	
	/**
	 * 清除被转义的html标签
	 * @param content
	 * @return 除去html标签的文本
	 */
	public static String cleanHtmlTag(String content){
		String tagHtml =  html(content);
		return delHTMLTag(tagHtml);
	}
	
	
	/**
	 * list转换字符串
	 */
	public static String listToString(List<?> list, String separator) {
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i)).append(separator);
			}
			String temp = sb.toString();
			return temp.substring(0, temp.length() - 1);
		}
		return null;
	}
	
	/**
	 * 一次md5加密
	 * 
	 * @param primalPass
	 *            原始密码.
	 * @return String.
	 */
	public static String md5(String primalPass) {
		String ret = primalPass;
		try {
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			ret = md5.encodePassword(primalPass, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 用于保存处理--数字格式化
	 * 
	 * @param number
	 *           待格式化的值
	 * @return String.
	 */
	public static String decimalFormat(Object number) {
		try {
			Double num = Double.valueOf(number.toString());
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			return decimalFormat.format(num);
		} catch (Exception e) {
			e.printStackTrace();
			return "0.00";
		}
	}
	
	/**
	 * 用于展示--数字格式化
	 * 
	 * @param number
	 *           待格式化的值
	 * @return String.
	 */
	public static String showDecimalFormat(Object number) {
		try {
			Double num = Double.valueOf(number.toString());
			DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
			return decimalFormat.format(num);
		} catch (Exception e) {
			e.printStackTrace();
			return "0.00";
		}
	}
	
	/**
	 * 数字格式化
	 * 
	 * @param number
	 *           待格式化的值
	 * @param pattern
	 *           待格式化的值
	 * @return String.
	 */
	public static String decimalFormat(Object number,String pattern) {
		try {
			DecimalFormat decimalFormat = new DecimalFormat("###.##");
			if(StringUtils.isNotEmpty(pattern)){
				decimalFormat = new DecimalFormat(pattern);
			}
			return decimalFormat.format(number);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
	
	
	/**
	 * 下载文件
	 * @param response
	 * @param basePath
	 * @param attachPath
	 * @param fileName
	 * @return
	 */
	public static String fileDownload(HttpServletResponse response, String basePath, String attachPath, String fileName) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition","attachment;fileName=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
			InputStream is = new FileInputStream(new File(basePath, attachPath));
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[1024 * 1024];
			int length;
			while ((length = is.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取上传文件的文件名
	 * @param file
	 * @return
	 */
	public static String getFileName(MultipartFile file) {
		String oldName = file.getOriginalFilename();
		if(oldName.contains("\\")){
			oldName = oldName.substring(oldName.lastIndexOf("\\") + 1);
		}
		return oldName;
	}
	
	//处理ie和fireFox下载的文件名的乱码问题
	public static String fileNameForEveryBrower(HttpServletRequest req, String fileName)
	{
		String ret = fileName;
		try
		{
			String agent = req.getHeader("USER-AGENT"); 
			if (StringUtils.isNotEmpty(agent) && agent.indexOf("MSIE") >= 0)
			{
				ret = URLEncoder.encode(ret, "UTF-8");
				
			}
//			else if (StringUtil.isNotEmpty(agent) && agent.indexOf("Mozilla") >= 0)
//			{
//				ret = MimeUtility.encodeText(ret, "UTF8", "B");  
//			}
			else
			{
				ret = URLEncoder.encode(ret, "UTF-8");
			}
		}
		catch (Exception ex)
		{
			//不处理
		}
		return ret;
	}
	/**
	 * 整除并对商进行进一法处理
	 * @param divisor   除数
	 * @param dividend  被除数
	 * @return int
	 */
	public static int div(BigDecimal divisor, BigDecimal dividend){
		int ret = 0;
		try
		{
			ret = divisor.divide(dividend, BigDecimal.ROUND_UP).intValue();
		}
		catch(Exception ex)
		{
			log.warn(ex.getMessage() + ex.fillInStackTrace());
		}
		return ret;
	}
	/**
	 * float类型数字的计算
	 * @param t1   float
	 * @param t2   float
	 * @param type  add  subtract multiply  divide
	 * @return
	 */
	public static float calFloatValue(float t1, float t2, String type) {
		BigDecimal a = new BigDecimal(String.valueOf(t1));
		BigDecimal b = new BigDecimal(String.valueOf(t2));
		float retValue = 0f;
		switch (type) {
			case "add":
				retValue = a.add(b).floatValue();
				break;
			case "subtract":
				retValue = a.subtract(b).floatValue();
				break;
			case "multiply":	
				retValue = a.multiply(b).floatValue();
				break;
			case "divide":	
				retValue = a.divide(b).floatValue();
				break;
		};
		return retValue;
	}

		
	public static void main(String[] args) {
		BigDecimal bd2 = new BigDecimal(31);
    	BigDecimal bd3 = new BigDecimal(32);
    	
    	for (int i =0; i < 12; i++) {
    		System.out.println(getUUID());
    	}
    	
	}	
	
}
