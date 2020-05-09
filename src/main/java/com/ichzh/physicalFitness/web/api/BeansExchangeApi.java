package com.ichzh.physicalFitness.web.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.service.AllocateRangeQueryRecordService;
import com.ichzh.physicalFitness.service.JzdSchoolQueryRecordService;
import com.ichzh.physicalFitness.service.PrimSchoolExportQueryRecordService;
import com.ichzh.physicalFitness.service.RecruitRangeQueryRecordService;
import com.ichzh.physicalFitness.service.SchoolDetailQueryRecordService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/exchange"})
public class BeansExchangeApi {

	@Autowired
	UserService userService;
	@Autowired
	JzdSchoolQueryRecordService jzdSchoolQueryRecordService;
	@Autowired
	SchoolDetailQueryRecordService schoolDetailQueryRecordService;
	@Autowired
	AllocateRangeQueryRecordService allocateRangeQueryRecordService;
	@Autowired
	PrimSchoolExportQueryRecordService primSchoolExportQueryRecordService;
	@Autowired
	RecruitRangeQueryRecordService recruitRangeQueryRecordService;
	
	/**
	 * 用豆交换某个API的一次使用机会
	 * @param apiCode  
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/exchangeCommodity", method= {RequestMethod.POST})
	public OperaResult exchangeCommodity(@RequestParam(value = "apiCode", required = true) Integer apiCode,
			HttpServletRequest request) {
		OperaResult result = new OperaResult();
		try
		{
			Member currentLoginUser = userService.queryNewestMember(request);
			if (currentLoginUser == null || StringUtils.isEmpty(currentLoginUser.getMemberId())) {
				result.setResultCode(OperaResult.RESULT_CODE_LOGIN_TIMEOUT);
				result.setResultDesc("登录已超时");
				return result;
			}
			//交换居住地查询学校一次
			if (Constant.DICT_ID_110005 == apiCode.intValue()) {
				return jzdSchoolQueryRecordService.exchangeOneQuery(currentLoginUser);
			}
			//交换查询一个学校详情的权限
			if (Constant.DICT_ID_110010 == apiCode.intValue()) {
				return schoolDetailQueryRecordService.exchangeOneQuery(currentLoginUser);
			}
			//交换派位范围查询
			if (Constant.DICT_ID_110006 == apiCode.intValue()) {
				return allocateRangeQueryRecordService.exchangeOneQuery(currentLoginUser);
			}
			//交换一次升学规划
			if (Constant.DICT_ID_110013 == apiCode.intValue()) {
				return primSchoolExportQueryRecordService.exchangeOneQuery(currentLoginUser);
			}
			//交换一次招生范围查询
			if (Constant.DICT_ID_110008 == apiCode.intValue()) {
				return recruitRangeQueryRecordService.exchangeOneQuery(currentLoginUser);
			}
			
			
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return result;
	}
}
