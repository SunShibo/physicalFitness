package com.ichzh.physicalFitness.web.api;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.ConditionInfo;
import com.ichzh.physicalFitness.repository.ConditionInfoRepository;
import com.ichzh.physicalFitness.service.ConditionInfoService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/conditioninfo"})
public class ConditionInfoApi {

	@Autowired
	ConditionInfoRepository conditionInfoRepository;
	@Autowired
	ConditionInfoService conditionInfoService;
	@Autowired
	UserService userService;
	
	/**
	 * 保存筛选条件
	 * @param serviceBlock    服务模块         
	 * @param studentStatus   学籍所在地
	 * @param householdRegistration 户籍所在地
	 * @param residence 居住
	 * @return
	 */
	@RequestMapping(value="/writeConditionInfo", method= {RequestMethod.POST})
	public OperaResult writeConditionInfo(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "studentStatus", required = false) Integer studentStatus,
			@RequestParam(value = "householdRegistration", required = false) Integer householdRegistration,
			@RequestParam(value = "residence", required = false) Integer residence, HttpServletRequest request) {
		
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
				conditionInfoService.updateConditionInfo(memberId, serviceBlock, town, studentStatus, householdRegistration, residence);
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
	 * 查询筛选条件历史记录
	 * @param serviceBlock   服务模块.
	 * @return
	 */
	@RequestMapping(value="/queryConditionInfo", method= {RequestMethod.POST})
	public OperaResult queryConditionInfo(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock, HttpServletRequest request) {
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
			}else {
				ConditionInfo conditionInfo = conditionInfoRepository.findByServiceBlockAndMemberId(serviceBlock, memberId);
				result.setResultCode(OperaResult.Success);
				result.setData(conditionInfo);
			}
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
}
