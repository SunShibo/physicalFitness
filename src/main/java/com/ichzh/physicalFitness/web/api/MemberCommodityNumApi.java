package com.ichzh.physicalFitness.web.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AllocateRangeQueryRecord;
import com.ichzh.physicalFitness.model.JzdSchoolQueryRecord;
import com.ichzh.physicalFitness.model.MemberCommodityNum;
import com.ichzh.physicalFitness.model.RecruitRangeQueryRecord;
import com.ichzh.physicalFitness.repository.AllocateRangeQueryRecordRepository;
import com.ichzh.physicalFitness.repository.JzdSchoolQueryRecordRepository;
import com.ichzh.physicalFitness.repository.RecruitRangeQueryRecordRepository;
import com.ichzh.physicalFitness.repository.RecruitRangeQueryResultRecordRepository;
import com.ichzh.physicalFitness.service.MemberCommodityNumService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/membercommoditynum"})
public class MemberCommodityNumApi {

	@Autowired
	UserService userService;
	@Autowired
	MemberCommodityNumService memberCommodityNumService;
	@Autowired
	JzdSchoolQueryRecordRepository jzdSchoolQueryRecordRepository;
	@Autowired
	AllocateRangeQueryRecordRepository allocateRangeQueryRecordRepository;
	@Autowired
	RecruitRangeQueryResultRecordRepository recruitRangeQueryResultRecordRepository;
	@Autowired
	RecruitRangeQueryRecordRepository recruitRangeQueryRecordRepository;
	
	/**
	 * 居住地查询学校：获取剩下查询居住地的个数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getJzdSchoolQueryNum", method= {RequestMethod.POST})
	public OperaResult getJzdSchoolQueryNum(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//获得当前登录用户
		String memberId = userService.getCurrentLoginUserId(request);
		if (memberId == null) {
			result.setResultCode(OperaResult.Success);
			//-1: 当前用户的登录状态已失效
			resultData.put("query_num", "-1");
			result.setData(resultData);
		}else {
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110005);
			if (memberCommodityNum != null) {
				//计算当日已经查询的居住地次数
				int currentDayQueryNum = 0;
				//已经查询的小区数
				List<JzdSchoolQueryRecord> jzdSChoolQueryRecord = jzdSchoolQueryRecordRepository.
						queryByMemberIdAndRecordTime(memberId, new Date());
				if (jzdSChoolQueryRecord != null) {
					currentDayQueryNum = jzdSChoolQueryRecord.size();
				}
				
				//查询次数
				int queryNum = 0;
				//免费的次数
				Integer freeNum = memberCommodityNum.getFreeNum();
				Integer buyExchangeNum = memberCommodityNum.getBuyExchangeNum();
				if (freeNum != null) {
					int leftFreeNum = freeNum - currentDayQueryNum;
					if (leftFreeNum < 0) {
						leftFreeNum = 0;
					}
					queryNum += leftFreeNum;
				}
				if (buyExchangeNum != null) {
					queryNum += buyExchangeNum.intValue();
				}
				result.setResultCode(OperaResult.Success);
				resultData.put("query_num", queryNum+"");
				result.setData(resultData);
			}else {
				result.setResultCode(OperaResult.Error);
				resultData.put("query_num", "0");
				result.setData(resultData);
			}
		}
		
		return result;
	}
	
	
	/**
	 * 派位范围查询：获取剩下查询学校的个数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAllocateRangeQueryNum", method= {RequestMethod.POST})
	public OperaResult getAllocateRangeQueryNum(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//获得当前登录用户
		String memberId = userService.getCurrentLoginUserId(request);
		if (memberId == null) {
			result.setResultCode(OperaResult.Success);
			//-1: 当前用户的登录状态已失效
			resultData.put("query_num", "-1");
			result.setData(resultData);
		}else {
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110006);
			if (memberCommodityNum != null) {
				
				//计算当日已经查询的派位范围次数
				int currentDayQueryNum = 0;
				//已经查询的小区数
				List<AllocateRangeQueryRecord> allocateRangeQueryRecords = allocateRangeQueryRecordRepository.
						queryByMemberIdAndRecordTime(memberId, new Date());
				if (allocateRangeQueryRecords != null) {
					currentDayQueryNum = allocateRangeQueryRecords.size();
				}
				
				//查询次数
				int queryNum = 0;
				//免费的次数
				Integer freeNum = memberCommodityNum.getFreeNum();
				Integer buyExchangeNum = memberCommodityNum.getBuyExchangeNum();
				if (freeNum != null) {
					
					int leftFreeNum = freeNum - currentDayQueryNum;
					if (leftFreeNum < 0) {
						leftFreeNum = 0;
					}
					queryNum += leftFreeNum;
				}
				if (buyExchangeNum != null) {
					queryNum += buyExchangeNum.intValue();
				}
				result.setResultCode(OperaResult.Success);
				resultData.put("query_num", queryNum+"");
				result.setData(resultData);
			}else {
				result.setResultCode(OperaResult.Error);
				resultData.put("query_num", "0");
				result.setData(resultData);
			}
		}
		
		return result;
	}
	
	/**
	 * 招生范围查询：获取剩下查询学校的个数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getRecruitRangeQueryNum", method= {RequestMethod.POST})
	public OperaResult getRecruitRangeQueryNum(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//获得当前登录用户
		String memberId = userService.getCurrentLoginUserId(request);
		if (memberId == null) {
			result.setResultCode(OperaResult.Success);
			//-1: 当前用户的登录状态已失效
			resultData.put("query_num", "-1");
			result.setData(resultData);
		}else {
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110008);
			if (memberCommodityNum != null) {
				
				//计算当日已经查询的派位范围次数
				int currentDayQueryNum = 0;
				//已经查询的小区数
				List<RecruitRangeQueryRecord> recruitRangeQueryRecords = recruitRangeQueryRecordRepository.
						queryByMemberIdAndRecordTime(memberId, new Date());
				if (recruitRangeQueryRecords != null) {
					currentDayQueryNum = recruitRangeQueryRecords.size();
				}
				
				
				//查询次数
				int queryNum = 0;
				//免费的次数
				Integer freeNum = memberCommodityNum.getFreeNum();
				Integer buyExchangeNum = memberCommodityNum.getBuyExchangeNum();
				if (freeNum != null) {
					int leftFreeNum = freeNum - currentDayQueryNum;
					if (leftFreeNum < 0) {
						leftFreeNum = 0;
					}
					queryNum += leftFreeNum;
				}
				if (buyExchangeNum != null) {
					queryNum += buyExchangeNum.intValue();
				}
				result.setResultCode(OperaResult.Success);
				resultData.put("query_num", queryNum+"");
				result.setData(resultData);
			}else {
				result.setResultCode(OperaResult.Error);
				resultData.put("query_num", "0");
				result.setData(resultData);
			}
		}
		
		return result;
	}	
}
