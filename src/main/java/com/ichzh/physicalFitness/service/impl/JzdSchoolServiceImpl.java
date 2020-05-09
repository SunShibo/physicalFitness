package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.dto.RangeDto;
import com.ichzh.physicalFitness.model.JzdSchool;
import com.ichzh.physicalFitness.model.RecruitRangeQueryResultRecord;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.JzdSchoolRepository;
import com.ichzh.physicalFitness.repository.RecruitRangeQueryResultRecordRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.JzdSchoolService;
import com.ichzh.physicalFitness.service.RecruitRangeQueryResultRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("jzdSchoolService")
public class JzdSchoolServiceImpl implements JzdSchoolService {

	@Autowired
	ICacheApplicationService cacheApplicationService;
	@Autowired
	JzdSchoolRepository jzdSchoolRepository;
	@Autowired
	RecruitRangeQueryResultRecordRepository recruitRangeQueryResultRecordRepository;
	@Autowired
	RecruitRangeQueryResultRecordService recruitRangeQueryResultRecordService;
	
	/**
	 * 为居住地对应的学校设置其它信息.
	 * @param jzdSchooles  List<JzdSchool>
	 * @param memberId  当前登录会员标识号.
	 */
	public void setOtherData(List<JzdSchool> jzdSchooles, String memberId) {
		
		if (jzdSchooles != null && jzdSchooles.size() > 0 && StringUtils.isNotEmpty(memberId)) {
			for (JzdSchool oneJzdSchool : jzdSchooles) {
				//学校代码
				String schoolCode = oneJzdSchool.getSchoolCode();
				//学校类型
				Integer serviceBlock = oneJzdSchool.getServiceBlock();
				SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(serviceBlock, schoolCode);
				if (schoolChoice != null) {
					oneJzdSchool.setChoiceId(schoolChoice.getChoiceId());
					oneJzdSchool.setSchoolId(schoolChoice.getChoiceId());
					oneJzdSchool.setLongitude(schoolChoice.getLongitude());
					oneJzdSchool.setDimension(schoolChoice.getDimension());
					
					boolean ifCollected = cacheApplicationService.checkSchoolIfCollected(memberId, serviceBlock, schoolChoice.getChoiceId());
					if (ifCollected) {
						oneJzdSchool.setColStatus(1);
					}else {
						oneJzdSchool.setColStatus(0);
					}
				}else {
					log.warn("根据学段和学校代码没有获取的雷达数据，学段为："+serviceBlock+" || 学籍代码为："+schoolCode);
				}
			}
		}
		
	}
	
	/**
	 * 将学校对应的居住地有三个项表达转为一个数据项表达.
	 * @param rangeDtos
	 */
	public void setAddress(List<RangeDto> rangeDtos) {
		if (rangeDtos != null && rangeDtos.size() > 0) {
			for (RangeDto oneRange : rangeDtos) {
				//街道名称
				String streetName = oneRange.getStreetName();
				//社区名称
				String communityName = oneRange.getCommunityName();
				//住址名称
				String detailAddress = oneRange.getDetailAddress();
				
				String address = "";
				if (StringUtils.isNotEmpty(streetName)) {
					address += streetName;
				}
				if (StringUtils.isNotEmpty(communityName)) {
					address += communityName;
				}
				if (StringUtils.isNotEmpty(detailAddress)) {
					address += detailAddress;
				}
				oneRange.setAddress(address);
			}
		}
		
	}

	/**
	 * 招生范围列表页查询更过地址
	 * @param serviceBlock    入学阶段
	 * @param town            入学区域
	 * @param querySchoolName 学校名称
	 * @return
	 */
	public Map<String, Object> queryStreetCommunityAddress4More(Integer serviceBlock, Integer town, String querySchoolName, String memberId) {
		
		Map<String, Object> retData = new HashMap<String, Object>();
		
		// 所有的地址
		List<RangeDto> recruitRanges =  jzdSchoolRepository.queryStreetCommunityAddress(serviceBlock, town,querySchoolName);
		this.setAddress(recruitRanges);
		// 所有地址的个数
		int allAddressNum = recruitRanges.size();
		
		// 当前会员已经查询过的地址
		List<RecruitRangeQueryResultRecord> recruitRangeQueryResultRecords = recruitRangeQueryResultRecordRepository.findByMemberIdAndSchoolName(memberId, querySchoolName);
		// 已经查询的地址个数
		int queriedAddressNum = 0;
		if (recruitRangeQueryResultRecords != null && recruitRangeQueryResultRecords.size() > 0) {
			queriedAddressNum = recruitRangeQueryResultRecords.size();
		}
		
		if (allAddressNum - queriedAddressNum > 2) {
			retData.put("ifHasMore", "1");
		}else {
			retData.put("ifHasMore", "0");
		}
		// 从所有地址中最多两个未查询过的地址
		List<String> notQueryAddress = new ArrayList<String>();
		// 本次要返回的地址
		List<RangeDto> backQueryAddress = new ArrayList<RangeDto>();
		int countNum = 0;
		for (RangeDto oneRange :  recruitRanges) {
			if (checkAddressIfQueried(recruitRangeQueryResultRecords, oneRange.getAddress())) {
				backQueryAddress.add(oneRange);
			}else if(countNum < 2){
				notQueryAddress.add(oneRange.getAddress());
				backQueryAddress.add(oneRange);
				countNum++;
			}
		}
		
		//存在更多地址时
		if (countNum > 0) {
			recruitRangeQueryResultRecordService.executeAterRecruitRangeQuerySuccess(memberId, "", querySchoolName, notQueryAddress);
		}
		retData.put("data", backQueryAddress);
		
		return retData;
	}
	
	private boolean checkAddressIfQueried(List<RecruitRangeQueryResultRecord> recruitRangeQueryResultRecords, String address) {
		boolean ret = false;
		if (recruitRangeQueryResultRecords != null && recruitRangeQueryResultRecords.size() > 0) {
			for (RecruitRangeQueryResultRecord oneQueryResultRecord : recruitRangeQueryResultRecords) {
				if (oneQueryResultRecord.getAddress().equals(address)) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}
    

	
}
