package com.ichzh.physicalFitness.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class SysFuncController {

	/**
	 * 权限列表
	 * @return
	 */
	@RequestMapping("/sysfunclist")
	public String toFuncManagerPage(){
		return "sysfuncmanager/sysfunc";
	}
	/**
	 * 新增权限
	 * @return
	 */
	@RequestMapping("/sysfuncadd")
	public String toFuncCreatePage(){
		return "sysfuncmanager/sysfunc_add";
	}
	/**
	 * 编辑权限
	 * @return
	 */
	@RequestMapping("/sysfuncedit")
	public String toEditFunc(){
		return "sysfuncmanager/sysfunc_edit";
	}
}
