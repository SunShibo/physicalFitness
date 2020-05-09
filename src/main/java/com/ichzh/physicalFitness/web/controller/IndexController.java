/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ichzh.physicalFitness.web.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/index_page")
	public String toIndexPage(HttpSession session, Principal principal){
		log.info("test logback");
		return "index_page";
	}
	
	@RequestMapping("/sysdefaultpage")
	public String toSysdefaultPage(){
		return "sys_default_xu";
	}
	
	@RequestMapping("/ipaddress")
	public String toIpAddressPage(){
		return "ip_address";
	}
	/**
	 * 默认页面
	 * @return
	 */
	@RequestMapping("/sysdefault")
	public String toWhitePage(){
		return "sys_default";
	}
	
	
	@RequestMapping("/relogin")
	public String toReloginPage(){
		return "relogin";
	}
	
	@RequestMapping("/404")
	public String notfind(){
		return "errorpage/404";
	}
	
	@RequestMapping("/500")
	public String serverError(){
		return "errorpage/500";
	}
	
	@RequestMapping("/baiduRichTextEditor")
	public String toBaiduRichTextEditorPage(){
		return "baidu_richText_editor";
	}
	@RequestMapping("/singlefileupload")
	public String toSingleFileUploadPage(){
		return "single_file_upload_page";
	}
	@RequestMapping("/multifileupload")
	public String toMultiFileUploadPage(){
		return "multiple_file_upload_page";
	}
	
	@RequestMapping("/multifileupload4modal")
	public String toMultiFileUpload4modalPage(){
		return "multiple_file_upload4modal_page";
	}
	
	@RequestMapping("/multifileuploadwindow")
	public String toMultiFileUploadWindowPage(){
		return "multiple_file_uploadwindow_page";
	}

	@RequestMapping("/html_to_pdf")
	public String toHtmlConvertPdfPage() {
		return "html_to_pdf";
	}
}
