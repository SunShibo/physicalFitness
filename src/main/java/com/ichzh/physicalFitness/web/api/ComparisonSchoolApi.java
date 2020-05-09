package com.ichzh.physicalFitness.web.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.ComparisonSchool;
import com.ichzh.physicalFitness.repository.ComparisonSchoolRepository;
import com.ichzh.physicalFitness.service.ComparisonSchoolService;
import com.ichzh.physicalFitness.service.SchoolChoiceService;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.util.ComparisonSchoolComparator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/comparisonSchool"})
public class ComparisonSchoolApi {

	@Autowired
	UserService userService;
	@Autowired
	ComparisonSchoolService comparisonSchoolService;
	@Autowired
	ComparisonSchoolRepository comparisonSchoolRepository;
	@Autowired
	SchoolChoiceService schoolChoiceService;
	
	
	
	/**
	 * 保存对比学校
	 * @param schoolId   学校ID
	 * @param schoolType        学校类型  1： 中小学   2：幼儿园
	 * @param orderNum   显示顺序号
	 * @return
	 */
	@RequestMapping(value="/writeComparisonSchool", method= {RequestMethod.POST})
	public OperaResult writeComparisonSchool(@RequestParam(value = "schoolId", required = true) Integer schoolId,
			@RequestParam(value = "schoolType", required = true) Integer schoolType, 
			@RequestParam(value = "orderNum", required = true) Integer orderNum,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Success);
				//1: 写入失败，当前用户的登录状态已失效
				resultData.put("write_result", "0");
				result.setData(resultData);
			}else {				
				comparisonSchoolService.writeComSchool(memberId, schoolId, schoolType, orderNum);				
				result.setResultCode(OperaResult.Success);
				//2: 写入成功.
				resultData.put("write_result", "1");
				result.setData(resultData);
			}
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	/**
	 * 查询当前用户的对比校
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryComparisonSchool", method= {RequestMethod.POST})
	public OperaResult queryComparisonSchool(HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
			}else {				
				
				List<ComparisonSchool> comSchooles = comparisonSchoolRepository.findByMemberId(memberId);
				Collections.sort(comSchooles, new ComparisonSchoolComparator());
				
				//设置学校的热力值
				comparisonSchoolService.setSchoolHeatingPowerValue(comSchooles);
				
				result.setResultCode(OperaResult.Success);
				result.setData(comSchooles);
			}
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	
	/**
	 * 删除当前用户的对比校
	 * @param schoolType 学校类型  1： 中小学   2：幼儿园
	 * @param schoolId   学校标识号.  
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delComparisonSchool", method= {RequestMethod.POST})
	public OperaResult queryComparisonSchool(@RequestParam(value = "schoolId", required = true) Integer schoolId,
			@RequestParam(value = "schoolType", required = true) Integer schoolType, 
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Success);
				//0:删除失败
				resultData.put("delete_result", "0");
				result.setData(resultData);
			}else {				
				
				comparisonSchoolRepository.deleteBy(memberId, schoolType, schoolId);
				result.setResultCode(OperaResult.Success);
				//0:删除成功
				resultData.put("delete_result", "1");
				result.setData(resultData);
				
			}
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
}
