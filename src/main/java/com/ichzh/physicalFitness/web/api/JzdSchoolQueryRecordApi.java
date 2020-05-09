package com.ichzh.physicalFitness.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.JzdSchoolQueryRecord;
import com.ichzh.physicalFitness.repository.JzdSchoolQueryRecordRepository;
import com.ichzh.physicalFitness.service.JzdSchoolQueryRecordService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/jzdschoolQueryRecord"})
public class JzdSchoolQueryRecordApi {

	@Autowired
	UserService userService;
	@Autowired
	JzdSchoolQueryRecordRepository jzdSchoolQueryRecordRepository;
	@Autowired
	JzdSchoolQueryRecordService jzdSchoolQueryRecordService;
	
	@RequestMapping(value="/queryJzdSchoolQueryRecord", method= {RequestMethod.POST})
	public OperaResult queryJzdSchoolQueryRecord(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<JzdSchoolQueryRecord> jzdSChoolQueryRecords = jzdSchoolQueryRecordRepository.
					findByMemberId(memberId);
			//设置区名称
			jzdSchoolQueryRecordService.setTownName(jzdSChoolQueryRecords);
			
			result.setResultCode(OperaResult.Success);
			result.setData(jzdSChoolQueryRecords);
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
}
