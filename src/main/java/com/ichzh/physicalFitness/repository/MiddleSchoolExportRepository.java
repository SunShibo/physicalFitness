package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.MiddleSchoolExport;
import com.ichzh.physicalFitness.repository.ext.IMiddleSchoolExportRepositoryExt;

@Repository
public interface MiddleSchoolExportRepository extends BaseRepository<MiddleSchoolExport, Integer>, IMiddleSchoolExportRepositoryExt{

	/**
	 * 根据初中学校代码查询初中校出口信息.
	 * @param middleSchoolCode  小学学校代码.
	 * @param year  年份.
	 * @return List<MiddleSchoolExport>.
	 */
	public List<MiddleSchoolExport> findByMiddleSchoolCodeAndYear(String middleSchoolCode, Integer year);
}
