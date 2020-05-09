package com.ichzh.physicalFitness.service;

import java.util.List;
import java.util.Map;

import com.ichzh.physicalFitness.dto.RangeDto;
import com.ichzh.physicalFitness.model.JzdSchool;

public interface JzdSchoolService {

	/**
	 * 为居住地对应的学校设置其它信息.
	 * @param jzdSchooles  List<JzdSchool>
	 * @param memberId  当前登录会员标识号.
	 */
	public void setOtherData(List<JzdSchool> jzdSchooles, String memberId);
	/**
	 * 将学校对应的居住地有三个项表达转为一个数据项表达.
	 * @param rangeDtos
	 */
	public void setAddress(List<RangeDto> rangeDtos);
	/**
	 * 招生范围列表页查询更过地址
	 * @param serviceBlock    入学阶段
	 * @param town            入学区域
	 * @param querySchoolName 学校名称
	 * @return
	 */
	public Map<String, Object> queryStreetCommunityAddress4More(Integer serviceBlock, Integer town, String querySchoolName, String memberId);
}
