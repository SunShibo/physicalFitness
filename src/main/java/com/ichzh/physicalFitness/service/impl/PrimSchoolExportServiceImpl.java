package com.ichzh.physicalFitness.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.PrimSchoolExport;
import com.ichzh.physicalFitness.service.PrimSchoolExportService;


@Service("primSchoolExportService")
public class PrimSchoolExportServiceImpl implements PrimSchoolExportService {

	/**
	 * 根据学校代码和年查询小学出口 
	 * @param schoolCode 小学学校代码.
	 * @param year  年度.
	 * @return List<PrimSchoolExport>.
	 */
	public List<PrimSchoolExport> findByPrimSchoolCodeAndYear(String schoolCode, Integer year) {
		
		return null;
	}

	
}
