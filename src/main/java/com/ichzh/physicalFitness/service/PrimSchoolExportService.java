package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.PrimSchoolExport;

public interface PrimSchoolExportService {

	/**
	 * 根据学校代码和年查询小学出口 
	 * @param schoolCode 小学学校代码.
	 * @param year  年度.
	 * @return List<PrimSchoolExport>.
	 */
	public List<PrimSchoolExport> findByPrimSchoolCodeAndYear(String schoolCode, Integer year);
	
}
