package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.dto.SchoolExportDto;

public interface IMiddleSchoolExportRepositoryExt {

	
	/**
	 * 查询某个初中出口数据最近三年是哪几年
	 * @param schoolCode  初中学校代码.
	 * @return List<Integer>.
	 */
	public List<Integer> queryYearOfMiddleSchoolExportLatestThree(String schoolCode);
	/**
	 * 查询其它出口学校列表数据
	 * @param schoolCode  初中学校代码
	 * @param year 出口数据所属年份
	 * @return List<SchoolExportDto>.
	 */
	public List<SchoolExportDto> queryOtherExportSchoolListData(String schoolCode, Integer year);
}
