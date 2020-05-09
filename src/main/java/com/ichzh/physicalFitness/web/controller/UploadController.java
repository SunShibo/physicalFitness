/**
 * 
 */
package com.ichzh.physicalFitness.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author audin
 *
 */
@Controller
public class UploadController {

	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public String showUpload() {
		return "upload";
	}
	
	/*
	 * 多文件上传 MultipartFile[] files，页面中设置多个同名file input即可
	 * 也支持直接使用javax.servlet.http.Part代替MultipartFile
	 */
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String doUpload(Model model, @RequestParam("file") MultipartFile file) {
		model.addAttribute("file", file);
		return "upload";
	}
	
//	@RequestMapping(value="/upload", method=RequestMethod.POST)
//	public String doUpload(Model model, @RequestParam("file") Part file) {
//		model.addAttribute("file", file);
//		return "upload";
//	}
	
	@RequestMapping(value="/download",method = RequestMethod.GET, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	public FileSystemResource doDownload(HttpServletResponse response) {
		// 在客户端显示的文件名，尽量避免中文文件名，如必须，则需要根据不同浏览器分别处理
		response.setHeader("Content-Disposition", "attachment;filename=WebCalendar-1.2.7.tar.gz");
		return new FileSystemResource("/Users/audin/Downloads/WebCalendar-1.2.7.tar.gz");
	}
}
