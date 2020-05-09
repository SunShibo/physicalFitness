package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.dto.SchoolExportDto;
import com.ichzh.physicalFitness.dto.SchoolExportLevelDto;
import com.ichzh.physicalFitness.model.MiddleSchoolExport;
import com.ichzh.physicalFitness.model.PrimSchoolExport;

public interface SchoolExportService {

	/**
	 * 组装小学出口数据._按每个出口初中校的占比 .
	 * @param primSchoolExportes List<PrimSchoolExport>.
	 * @return
	 */
	public List<SchoolExportDto> assemblePrimSchoolExportData(List<PrimSchoolExport> primSchoolExportes);
	
	/**
	 * 组装小学出口数据._按每个出口初中校等级 .
	 * @param primSchoolExportes List<PrimSchoolExport>.
	 * @return
	 */
	public List<SchoolExportLevelDto> assemblePrimSchoolExportDataByLevel(List<PrimSchoolExport> primSchoolExportes);
	
	
	/**
	 * 设置初中校在学校优选数据中对应的其它信息.
	 * @param schoolExportDtoes
	 */
	public void setSchoolChoiceInfo(String memberId, List<SchoolExportDto> schoolExportDtoes);
	
	
	
	/**
	 * 组装初中校出口数据._按每个出口初中校的占比 .
	 * @param middleSchoolExportes List<MiddleSchoolExport>.
	 * @return
	 */
	public List<SchoolExportDto> assembleMiddleSchoolExportData(List<MiddleSchoolExport> middleSchoolExportes);
	
	
	/**
	 * 组装初中出口数据._按每个出口高中校等级 .
	 * @param middleSchoolExportes List<MiddleSchoolExport>.
	 * @return
	 */
	public List<SchoolExportLevelDto> assembleMiddleSchoolExportDataByLevel(List<MiddleSchoolExport> middleSchoolExportes);
	
	/**
	 * 获取学校出口树信息.
	 * @param serviceBlock  小学：10002  初中：10003
	 * @param schoolCode  学校代码.
	 * @param year  年度.
	 * @return SchoolExportDto.
	 */
	public SchoolExportDto getSchoolExportTreeData(Integer serviceBlock, String schoolCode, Integer year);
	
}
