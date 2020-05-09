package com.ichzh.physicalFitness.web.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.UploadImageActionEnter;

@RestController
@RequestMapping("/ueditor")
public class BaiduUEAction {
	
	@RequestMapping(value="/controller")
    public String config(HttpServletRequest request, HttpServletResponse response) {
 
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        return new UploadImageActionEnter(request, rootPath).exec();
    }
	
}
