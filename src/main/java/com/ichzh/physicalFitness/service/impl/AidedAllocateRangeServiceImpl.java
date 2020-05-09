package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.AidedAllocateRangePrimSchoolDto;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.model.SchoolCollection;
import com.ichzh.physicalFitness.service.AidedAllocateRangeService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;

@Service("aidedAllocateRangeService")
public class AidedAllocateRangeServiceImpl implements AidedAllocateRangeService {

	@Autowired
	ICacheApplicationService cacheApplicationService;
	/**
	 * 将给定的多个中学按派位类型进行分组
	 * @param addmisssionTimes
	 * @return Map<String, List<AdmissionTime>>  key 分组名 value 分组下对应的入学时间
	 */
	public Map<String, List<AidedAllocateRangePrimSchoolDto>> groupRangeMiddSchool(
			List<AidedAllocateRangePrimSchoolDto> middSchooles) {
		
		Map<String, List<AidedAllocateRangePrimSchoolDto>> ret = new LinkedHashMap<String, List<AidedAllocateRangePrimSchoolDto>>();
		if (middSchooles != null && middSchooles.size() > 0) {
			for (AidedAllocateRangePrimSchoolDto oneMiddSchool : middSchooles) {
				Integer allocateKind = oneMiddSchool.getAidedAllocateKind();
				//获得派位类型名称
				String allocateName = cacheApplicationService.getDictName(allocateKind);
				
				List<AidedAllocateRangePrimSchoolDto> currentMiddSchooles = ret.get(allocateName);
				if (currentMiddSchooles == null) {
					currentMiddSchooles = new ArrayList<AidedAllocateRangePrimSchoolDto>();
				}
				currentMiddSchooles.add(oneMiddSchool);
				ret.put(allocateName, currentMiddSchooles);
			}
		}
		return ret;
	}
	
	/**
	 * 为中学派位范围中的初中校补充是否收藏及学校优选中的主键信息.
	 * @param middSchooles List<AidedAllocateRangePrimSchoolDto>.
	 */
	public void setOtherDataForMiddleSchool(String memberId, List<AidedAllocateRangePrimSchoolDto> middSchooles) {
		
		if (middSchooles != null && middSchooles.size() > 0) {
			for (AidedAllocateRangePrimSchoolDto oneSchoolDto : middSchooles) {
				
				//中学代码
				String schoolCode = oneSchoolDto.getSchoolCode();
				
				// 学校代码对应的学校优选主键信息
				SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(Constant.DICT_ID_10003, schoolCode);
				
				if (schoolChoice != null) {
					oneSchoolDto.setSchoolId(schoolChoice.getChoiceId());
					oneSchoolDto.setDimension(schoolChoice.getDimension());
					oneSchoolDto.setLongitude(schoolChoice.getLongitude());
					
					if(cacheApplicationService.checkSchoolIfCollected(memberId, Constant.DICT_ID_10003, oneSchoolDto.getSchoolId())) {
						oneSchoolDto.setColStatus(1);
					}else {
						oneSchoolDto.setColStatus(0);
					}
				}
				
				
				
				
			}
		}
	}

	
	
}
