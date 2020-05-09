package com.ichzh.physicalFitness.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainsiteErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";
    @RequestMapping(value=ERROR_PATH)
    public String handleError(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
    	if (response.getStatus()==500) {
    		return "errorpage/500";
    	}else if (response.getStatus()==404) {
    		return "errorpage/404";
    	}else if (response.getStatus()==400) {
    		return "errorpage/400";
    	}
    	return "errorpage/error";
    }
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}