package com.ichzh.physicalFitness.web.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AllocateRangeQueryRecord;
import com.ichzh.physicalFitness.repository.AllocateRangeQueryRecordRepository;
import com.ichzh.physicalFitness.service.AllocateRangeQueryRecordService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/allocateRangeQueryRecord"})
public class AllocateRangeQueryRecordApi {

	@Autowired
	UserService userService;
	@Autowired
	AllocateRangeQueryRecordRepository allocateRangeQueryRecordRepository;
	@Autowired
	AllocateRangeQueryRecordService allocateRangeQueryRecordService;
	
	@RequestMapping(value="/queryAllocateRangeQueryRecord", method= {RequestMethod.POST})
	public OperaResult queryAllocateRangeQueryRecord(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<AllocateRangeQueryRecord> allocateRangeQueryRecords = allocateRangeQueryRecordRepository.
					findByMemberId(memberId);
			// 设置区名称
			allocateRangeQueryRecordService.setTownName(allocateRangeQueryRecords);
			
			result.setResultCode(OperaResult.Success);
			result.setData(allocateRangeQueryRecords);
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
}
