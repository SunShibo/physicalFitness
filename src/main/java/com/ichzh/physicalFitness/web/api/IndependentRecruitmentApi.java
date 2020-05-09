package com.ichzh.physicalFitness.web.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.IndependentRecruitment;
import com.ichzh.physicalFitness.repository.IndependentRecruitmentRepository;
import com.ichzh.physicalFitness.service.IndependentRecruitmentService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/independentrecruitment"})
public class IndependentRecruitmentApi {

	@Autowired
	private IndependentRecruitmentRepository independentRecruitmentRepository;
	@Autowired
	private IndependentRecruitmentService independentRecruitmentService;
	@Autowired
	UserService userService;
	
	/**
	 * 查询自主招生
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param isCity               是否全市招生
	 * @return
	 */
	@RequestMapping(value="/queryIndependentRecruitment", method= {RequestMethod.POST})
	public OperaResult queryIndependentRecruitment(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "isCity", required = true) Integer isCity,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<IndependentRecruitment> independentRecruitments = independentRecruitmentRepository.queryBy(serviceBlock, town, isCity);
			
			//设置经纬度相关信息
			independentRecruitmentService.setOtherData(independentRecruitments, memberId);
			
			result.setResultCode(OperaResult.Success);
			result.setData(independentRecruitments);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
