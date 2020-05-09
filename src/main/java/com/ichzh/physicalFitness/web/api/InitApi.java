package com.ichzh.physicalFitness.web.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.dto.Breadcrumb;
import com.ichzh.physicalFitness.util.CommonUtil;

@RestController
@Slf4j
@RequestMapping("/ajax")
public class InitApi {

	/**
	 * 将导航信息存储到session中
	 * @param breadcrumb  JSON对象
	 *         {menuName1 : null,
        		menuUrl1 : null,
        		menuName2 : null,
        		menuUrl2 : null,
        		title : null}转换为的字符串
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/set_breadcrumb", method=RequestMethod.POST)
	private OperaResult setBreadcrumbToSession(@RequestParam("breadcrumb") String breadcrumb, HttpServletRequest request){
		OperaResult ret = new OperaResult(); 
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			JavaType breadcrumbJavaType = CommonUtil.getCollectionType(mapper, Breadcrumb.class);
			Breadcrumb breadcrumbObj = (Breadcrumb)mapper.readValue(breadcrumb, breadcrumbJavaType);
			HttpSession session = request.getSession();
			session.setAttribute(Constant.CURRENT_BREADCRUMB, breadcrumbObj);
			ret.setResultCode(Constant.OPT_RESULT_CODE_SUCCESS+"");
		}
		catch(Exception ex)
		{
			ret.setResultCode(Constant.OPT_RESULT_CODE_FAIL+"");
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return ret;
	}
	/**
	 * 从session中获取当前导航信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/get_breadcrumb", method=RequestMethod.GET)
	private Breadcrumb getBreadcrumbFromSession(HttpServletRequest request){
		Breadcrumb ret = new Breadcrumb();
		try
		{
			ret = (Breadcrumb)request.getSession().getAttribute(Constant.CURRENT_BREADCRUMB);
		}
		catch(Exception ex)
		{
			log.warn(ex.getMessage() + ex.fillInStackTrace());
		}
		return ret;
	}
}
