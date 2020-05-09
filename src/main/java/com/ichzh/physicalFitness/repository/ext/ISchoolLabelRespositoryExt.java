package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.SchoolLabel;

public interface ISchoolLabelRespositoryExt {

	/**
	 * 根据学校代码查询学校标签：按顺序号升序排列
	 * @param schoolCode  学校代码
	 * @return List<SchoolLabel>.
	 */
	public List<SchoolLabel> querySchoolLabelBy(String schoolCode);
}
