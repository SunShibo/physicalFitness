package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.SchoolChoice;

public interface ISchoolChoiceRepositoryExt {

	/**
	 * 导数据到mongodb数据库里
	 * @return
	 */
	public boolean exportSchoolForMongodb();
	/**
	 * 通过学校名称查询中小学
	 * @param schoolName 学校名称
	 * @return
	 */
	public List<Object> querySchoolChoinceBy(String schoolName, Integer town);

}
