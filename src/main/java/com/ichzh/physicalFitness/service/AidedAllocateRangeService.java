package com.ichzh.physicalFitness.service;

import java.util.List;
import java.util.Map;

import com.ichzh.physicalFitness.dto.AidedAllocateRangePrimSchoolDto;

public interface AidedAllocateRangeService {

	/**
	 * 将给定的多个中学按派位类型进行分组
	 * @param addmisssionTimes
	 * @return Map<String, List<AdmissionTime>>  key 分组名 value 分组下对应的入学时间
	 */
	public Map<String, List<AidedAllocateRangePrimSchoolDto>> groupRangeMiddSchool(List<AidedAllocateRangePrimSchoolDto> middSchooles);
	/**
	 * 为中学派位范围中的初中校补充是否收藏及学校优选中的主键信息.
	 * @param middSchooles List<AidedAllocateRangePrimSchoolDto>.
	 */
	public void setOtherDataForMiddleSchool(String memberId, List<AidedAllocateRangePrimSchoolDto> middSchooles);
}
