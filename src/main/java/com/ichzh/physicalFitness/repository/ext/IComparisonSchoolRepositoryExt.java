package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.ComparisonSchool;
import com.ichzh.physicalFitness.model.SchoolChoice;

public interface IComparisonSchoolRepositoryExt {

	/**
	 * 查询某个会员的对比校.
	 * @param memberId  会员标识号.
	 * @return
	 */
	public List<SchoolChoice> queryComparisonSchoolBy(String memberId);
	
	/**
	 * 删除对比校
	 * @param memberId    会员标识号.
	 * @param schoolType  学校类型.  1： 中小学   2：幼儿园
	 * @param schoolId    幼儿园或中小学标识号.
	 */
	public void deleteBy(String memberId, Integer schoolType, Integer schoolId);
	
	/**
	 * 将当前会员收藏学校的显示序号增加1（当前学校除外）
	 * @param mermerId  会员标识号.
	 * @param schoolId  当前收藏的学校.
	 * @return
	 */
	public void updateOtherOrderNum(String mermerId, Integer schoolId);
	
}
