package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.PrimSchoolExport;
import com.ichzh.physicalFitness.repository.ext.IPrimSchoolExportRepositoryExt;

@Repository
public interface PrimSchoolExportRepository extends BaseRepository<PrimSchoolExport, Integer>, IPrimSchoolExportRepositoryExt{

	/**
	 * 根据小学学校代码查询小学出口信息.
	 * @param primSchoolCode  小学学校代码.
	 * @param year  年份.
	 * @return List<PrimSchoolExport>.
	 */
	public List<PrimSchoolExport> findByPrimSchoolCodeAndYear(String primSchoolCode, Integer year);
}
