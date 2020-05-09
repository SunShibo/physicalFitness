package com.ichzh.physicalFitness.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.MessageByLocaleService;
import com.ichzh.physicalFitness.service.MinioService;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.FileItem2;
import com.ichzh.physicalFitness.util.FileUploadResult;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping({"/api", "/notwxchart"})
@Slf4j
public class CommonApi{

	@Autowired
	private MessageByLocaleService messageByLocaleService;

	@Autowired
	private MinioService minioService;
	
	@Autowired
	private ICacheApplicationService cacheApplicationService;
	
	/**
	 * 保存页面上传的文件
	 * @param file     {@link MultipartFile}
	 * @param request  {@link HttpServletRequest}
	 * @return  {@link FileUploadResult}
	 */
	@RequestMapping(value="/common/upload_file", method={RequestMethod.POST})
	private FileUploadResult fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
		FileItem2 fileItem2 = new FileItem2(request.getServletContext(), file);
		FileUploadResult uploadResult = fileItem2.save();
		return uploadResult;
	}
	
	/**
	 * 刷新系统缓存
	 * @return
	 */
	@RequestMapping(value="/common/refresh_cache", method={RequestMethod.GET})
	private Map<String,Object> refreshCache(){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			cacheApplicationService.cacheData();
			result.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="/common/ip_address", method={RequestMethod.POST})
	private Map<String,Object> ip_address(HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			String ip = CommonUtil.getServerIp(request);
			result.put("ip", ip);
			result.put("result", "ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 下载文件
	 * uri 以 /images 开头时，无需登录也能访问，不暴露到外网，主要是给 pdf 加图片时用
	 * uri 以 /api 开头时，需要登录才能访问，暴漏到外网，主要是给 web 页面显示用
	 */
	@RequestMapping("/common/download_file")
	public void downloadFile(String objectName, HttpServletRequest request, HttpServletResponse response) {
		// images 不会被登录拦截，只能项目内部访问,限制 ip 必须为 127.0.0.1
		if (request.getServletPath().startsWith("/images") && !CommonUtil.getIp(request).equals("127.0.0.1")) {
			return;
		}
		try {
			minioService.downloadFileByDefaultBucket(request, response, objectName, objectName);
		} catch (Exception e) {
			log.error("文件下载失败", e);
		}
	}
}
