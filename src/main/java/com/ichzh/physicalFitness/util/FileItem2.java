package com.ichzh.physicalFitness.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author smileyang
 *
 */
public class FileItem2 {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileItem2.class);

	//原始文件名
	private String originalFileName;
	//临时存放文件的位置
	private String relativeFilePath;
	private ServletContext servletContext; 
	private MultipartFile file;
	/**
	 * 构建处理文件存储对象
	 * @param servletContext  {@link ServletContext}
	 * @param file            {@link MultipartFile}
	 */
	public FileItem2(ServletContext servletContext, MultipartFile file){
		this.servletContext = servletContext;
		this.file = file;
	};
	
	public FileUploadResult save(){
		FileUploadResult retValue = new FileUploadResult();
		BufferedOutputStream out = null;
		BufferedInputStream   in = null;
		try{
			//上传目录的绝对路径
			String realPathOfPublic = servletContext.getRealPath("uploads");
			//原始文件名
			String oriFileName= file.getOriginalFilename();
			//扩展名
			String extName = CommonUtil.getExtName(oriFileName);
			//保存的文件
			String fileFullPath = createFileDirNew(realPathOfPublic) + new Date().getTime() + "."+extName;
			out = new BufferedOutputStream(new FileOutputStream(new File(fileFullPath)));
			in = new BufferedInputStream(file.getInputStream());
			int b;
	        while ((b = in.read()) != -1) {
	            out.write(b);
	        }
	        in.close();
	        out.close();
	        
	        retValue.setFileUri(fileFullPath.substring(fileFullPath.indexOf("uploads")).replaceAll("\\\\", "/"));
			
		}catch (Exception ex){
			retValue.setFileUri("");
			retValue.setUploadFeedback(ex.getMessage()+ex.fillInStackTrace());
			logger.error(ex.getMessage() + ex.fillInStackTrace());
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
		}
		return  retValue;
		
	}
	
	/**
	 * 批量上传任务单（压缩包）
	 * @param username
	 * @return
	 */
	public FileUploadResult save4Batch(String username){
		FileUploadResult retValue = new FileUploadResult();
		BufferedOutputStream out = null;
		BufferedInputStream   in = null;
		try{
			//上传目录的绝对路径
			String realPathOfPublic = servletContext.getRealPath("uploads");
			//原始文件名
			String oriFileName= file.getOriginalFilename();
			//扩展名
			String extName = CommonUtil.getExtName(oriFileName);
			//保存的文件
			String fileFullPath = createFileDirNew(realPathOfPublic) + new Date().getTime() + "_" + username + "."+extName;
			out = new BufferedOutputStream(new FileOutputStream(new File(fileFullPath)));
			in = new BufferedInputStream(file.getInputStream());
			int b;
	        while ((b = in.read()) != -1) {
	            out.write(b);
	        }
	        in.close();
	        out.close();
	        
//	        if("jpg".equalsIgnoreCase(extName)||"jpeg".equalsIgnoreCase(extName)||"png".equalsIgnoreCase(extName)){
//	        	 ImageCompressUtil iu=new ImageCompressUtil(fileFullPath);
//	 			 iu.resizeByWidth(160, null);
//	        }
	        
	        retValue.setFileUri(fileFullPath.substring(fileFullPath.indexOf("uploads")).replaceAll("\\\\", "/"));
			
		}catch (Exception ex){
			retValue.setFileUri("");
			retValue.setUploadFeedback(ex.getMessage()+ex.fillInStackTrace());
			logger.error(ex.getMessage() + ex.fillInStackTrace());
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
		}
		return  retValue;
		
	}
	
	/*
	 * 上传学生任务单
	 */
	public FileUploadResult saveStudentTaskList(String eduId){
		FileUploadResult retValue = new FileUploadResult();
		BufferedOutputStream out = null;
		BufferedInputStream   in = null;
		try{
			//上传目录的绝对路径
			String realPathOfPublic = servletContext.getRealPath("uploads");
			//原始文件名
			String oriFileName= file.getOriginalFilename();
			//扩展名
			String extName = CommonUtil.getExtName(oriFileName);
			//保存的文件
			String fileFullPath = createFileDirNew(realPathOfPublic) + new Date().getTime() + "_" + eduId + "."+extName;
			out = new BufferedOutputStream(new FileOutputStream(new File(fileFullPath)));
			in = new BufferedInputStream(file.getInputStream());
			int b;
	        while ((b = in.read()) != -1) {
	            out.write(b);
	        }
	        in.close();
	        out.close();
	        
//	        if("jpg".equalsIgnoreCase(extName)||"jpeg".equalsIgnoreCase(extName)||"png".equalsIgnoreCase(extName)){
//	        	 ImageCompressUtil iu=new ImageCompressUtil(fileFullPath);
//	 			 iu.resizeByWidth(160, null);
//	        }
	        
	        retValue.setFileUri(fileFullPath.substring(fileFullPath.indexOf("uploads")).replaceAll("\\\\", "/"));
//	        throw new ZipException();
		}catch (Exception ex){
			retValue.setFileUri("");
			retValue.setUploadFeedback(ex.getMessage()+ex.fillInStackTrace());
			logger.error(ex.getMessage() + ex.fillInStackTrace());
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
		}
		return  retValue;
		
	}
	
	public FileUploadResult instSave(String insCode,String courseId,Integer isType){
		FileUploadResult retValue = new FileUploadResult();
		BufferedOutputStream out = null;
		BufferedInputStream   in = null;
		try{
			//上传目录的绝对路径
			String realPathOfPublic = servletContext.getRealPath("uploads");
			//原始文件名
			String oriFileName= file.getOriginalFilename();
			//扩展名
			String extName = CommonUtil.getExtName(oriFileName);
			//不可以上传gif
			if(!extName.toLowerCase().equals("jpg")&&!extName.toLowerCase().equals("jpeg")&&!extName.toLowerCase().equals("png")){
				retValue.setFileUri("");
				retValue.setUploadFeedback("只可上传jpg、jpeg、png格式的图片");
				return retValue;
			}
			//保存的文件
			String fileFullPath = createFileDirInst(realPathOfPublic,insCode,courseId) + new Date().getTime() + "."+extName.toLowerCase();
			out = new BufferedOutputStream(new FileOutputStream(new File(fileFullPath)));
			in = new BufferedInputStream(file.getInputStream());
			int b;
	        while ((b = in.read()) != -1) {
	            out.write(b);
	        }
	        in.close();
	        out.close();
	        if(isType!=null && isType == 1){
	        	if("jpg".equalsIgnoreCase(extName)||"jpeg".equalsIgnoreCase(extName)||"png".equalsIgnoreCase(extName)){
	        		ImageCompressUtil iu=new ImageCompressUtil(fileFullPath);
	        		iu.resizeByWidth(160, null);
	        	}
	        }
	        
	        retValue.setFileUri(fileFullPath.substring(fileFullPath.indexOf("uploads")).replaceAll("\\\\", "/"));
			
		}catch (Exception ex){
			retValue.setFileUri("");
			retValue.setUploadFeedback(ex.getMessage()+ex.fillInStackTrace());
			logger.error(ex.getMessage() + ex.fillInStackTrace());
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
		}
		return  retValue;
		
	}
	
	public FileUploadResult instVideoSave(Integer isType){
		FileUploadResult retValue = new FileUploadResult();
		BufferedOutputStream out = null;
		BufferedInputStream   in = null;
		try{
			//上传目录的绝对路径
			String realPathOfPublic = servletContext.getRealPath("uploads");
			//原始文件名
			String oriFileName= file.getOriginalFilename();
			//扩展名
			String extName = CommonUtil.getExtName(oriFileName);
			//保存的文件
			String fileFullPath = createVideoDirInst(realPathOfPublic) + new Date().getTime() + "."+extName;
			out = new BufferedOutputStream(new FileOutputStream(new File(fileFullPath)));
			in = new BufferedInputStream(file.getInputStream());
			int b;
	        while ((b = in.read()) != -1) {
	            out.write(b);
	        }
	        in.close();
	        out.close();
	        if(isType!=null && isType == 1){
	        	if("jpg".equalsIgnoreCase(extName)||"jpeg".equalsIgnoreCase(extName)||"png".equalsIgnoreCase(extName)){
	        		ImageCompressUtil iu=new ImageCompressUtil(fileFullPath);
	        		iu.resizeByWidth(160, null);
	        	}
	        }
	        
	        retValue.setFileUri(fileFullPath.substring(fileFullPath.indexOf("uploads")).replaceAll("\\\\", "/"));
			
		}catch (Exception ex){
			retValue.setFileUri("");
			retValue.setUploadFeedback(ex.getMessage()+ex.fillInStackTrace());
			logger.error(ex.getMessage() + ex.fillInStackTrace());
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage() + e.fillInStackTrace());
				}
			}
		}
		return  retValue;
		
	}
	
	/**
	 * 创建文件存放目录--课程视频
	 * 改动前：uploads/'userid'/'courseid'/...
	 * 改动后：uploads/institution/'userid'/'courseid'/...
	 * @param realPathOfPublic
	 * @return
	 */
	public static String createVideoDirInst(String realPathOfPublic){
		String ret = realPathOfPublic + File.separator+"institution"+ File.separator + "video" +
				File.separator;
		File file = new File(ret);
		if (!file.exists()){
			if (!file.mkdirs()){
				logger.warn("can not create catalog "+ ret);
			}
		}
		return ret;
	}
	
	public static boolean deleteFile(String relativeFilePath, ServletContext servletContext){
		boolean ret = false;
		try{
			//上传目录的绝对路径
			String realPathOfPublic = servletContext.getRealPath(relativeFilePath);
			File file = new File(realPathOfPublic);
			if (file.isFile() && file.exists()){
				file.delete();
			}
			ret = true;
		}catch(Exception ex){
			logger.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return ret;
	}
	
	public static String createFileDir(String realPathOfPublic){
		String ret = "";
		//文件存放目录uploads/yyyymm
		ret = realPathOfPublic + File.separator + CommonUtil.getCurrentYear()+
				File.separator +CommonUtil.getCurrentMonth()+
				File.separator+CommonUtil.getCurrentDay()+File.separator;
		File file = new File(ret);
		if (!file.exists()){
			if (!file.mkdirs()){
				logger.warn("can not create catalog "+ ret);
			}
		}
		return ret;
	}
	
	/**
	 * 创建文件存放目录--任务单/实践成果
	 * uploads/yyyy/mm/dd/
	 * @param realPathOfPublic
	 * @return
	 */
	public static String createFileDirNew(String realPathOfPublic){
		String ret = realPathOfPublic + File.separator+CommonUtil.getCurrentYear()+
				File.separator +CommonUtil.getCurrentMonth()+
				File.separator+CommonUtil.getCurrentDay()+File.separator;
		File file = new File(ret);
		if (!file.exists()){
			if (!file.mkdirs()){
				logger.warn("can not create catalog "+ ret);
			}
		}
		return ret;
	}
	/**
	 * 创建文件存放目录--机构资源文件
	 * 改动前：uploads/'userid'/'courseid'/...
	 * 改动后：uploads/institution/'userid'/'courseid'/...
	 * @param realPathOfPublic
	 * @return
	 */
	public static String createFileDirInst(String realPathOfPublic,String instCode,String courseId){
		String ret = realPathOfPublic + File.separator+"institution"+ File.separator+instCode+
				File.separator +courseId+
				File.separator;
		File file = new File(ret);
		if (!file.exists()){
			if (!file.mkdirs()){
				logger.warn("can not create catalog "+ ret);
			}
		}
		return ret;
	}
	
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public String getRelativeFilePath() {
		return relativeFilePath;
	}
	public void setRelativeFilePath(String relativeFilePath) {
		this.relativeFilePath = relativeFilePath;
	}
	
	
}

