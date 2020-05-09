package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.SchoolExportDto;
import com.ichzh.physicalFitness.dto.SchoolExportLevelDto;
import com.ichzh.physicalFitness.model.HighLevel;
import com.ichzh.physicalFitness.model.MiddleLevel;
import com.ichzh.physicalFitness.model.MiddleSchoolExport;
import com.ichzh.physicalFitness.model.PrimSchoolExport;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.MiddleSchoolExportRepository;
import com.ichzh.physicalFitness.repository.PrimSchoolExportRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.SchoolExportService;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.SchoolExportDtoComparator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("schoolExportService")
public class SchoolExportServiceImpl implements SchoolExportService{
	
	@Autowired
	ICacheApplicationService cacheApplicationService;
	@Autowired
	PrimSchoolExportRepository primSchoolExportRepository;
	@Autowired
	MiddleSchoolExportRepository middleSchoolExportRepository;
	@Autowired
	SchoolExportService schoolExportService;

	@Override
	public List<SchoolExportDto> assemblePrimSchoolExportData(List<PrimSchoolExport> primSchoolExportes) {
		
		List<SchoolExportDto> ret = new ArrayList<SchoolExportDto>();
		if (primSchoolExportes != null && primSchoolExportes.size() > 0) {
		    //占比小于或等于5%的 之和
			Float sumSxRatio = Float.valueOf("0.00");
			for (PrimSchoolExport onePrimSchoolExport :  primSchoolExportes) {
				// 升学占比
				Float currentRatio = onePrimSchoolExport.getSxRatio();
				//学校代码
				String currentSchoolCode = onePrimSchoolExport.getMiddleSchoolCode();
				//学校名称
				String currentSchoolName = onePrimSchoolExport.getMiddleSchoolName();
				//校区名称
				String currentCampusName = onePrimSchoolExport.getMiddleCampusName();
				
				if (currentRatio <= Float.valueOf("5.00")) {
					sumSxRatio = Float.valueOf(CommonUtil.add(currentRatio.toString(), sumSxRatio.toString()).toString());
					continue;
				}
				
				SchoolExportDto schoolExportDto = new SchoolExportDto();
				schoolExportDto.setSchoolCode(currentSchoolCode);
				schoolExportDto.setSchoolName(currentSchoolName+currentCampusName);
				schoolExportDto.setSxRatio(currentRatio);
				schoolExportDto.setSchoolId(cacheApplicationService.getSchoolChoiceIdBy(Constant.DICT_ID_10003, currentSchoolCode));
				schoolExportDto.setOther(0);
				
				ret.add(schoolExportDto);
						
			}
			
			if (sumSxRatio > Float.valueOf("0.00")) {
				SchoolExportDto schoolExportDto = new SchoolExportDto();
				schoolExportDto.setSxRatio(Float.valueOf(CommonUtil.div(sumSxRatio.toString(), "1", 2)));
				schoolExportDto.setSchoolName("其他学校 ");
				schoolExportDto.setOther(1);
				ret.add(schoolExportDto);
			}
		}
		
		return ret;
	}
	
	/**
	 * 组装小学出口数据._按每个出口初中校等级 .
	 * @param primSchoolExportes List<PrimSchoolExport>.
	 * @return
	 */
	public List<SchoolExportLevelDto> assemblePrimSchoolExportDataByLevel(List<PrimSchoolExport> primSchoolExportes) {
		
		Map<Integer, SchoolExportLevelDto> mapLevelRatio = new HashMap<Integer, SchoolExportLevelDto>();
		if (primSchoolExportes != null && primSchoolExportes.size() > 0) {
			for (PrimSchoolExport onePrimSchoolExport : primSchoolExportes) {
				//学校代码
				String middleSchoolCode = onePrimSchoolExport.getMiddleSchoolCode();
				//升学占比
				Float sxRatio = onePrimSchoolExport.getSxRatio();
				
				//学校代码对应的choiceId
				Integer schoolChoiceId = cacheApplicationService.getSchoolChoiceIdBy(Constant.DICT_ID_10003, middleSchoolCode);
				
				//学校等级
				MiddleLevel middleLevel = cacheApplicationService.getMiddleLevelBy(middleSchoolCode);
				if (middleLevel == null) {
					continue;
				}
				
				if (middleLevel.getLevel() == null) {
					middleLevel.setLevel(0);
				}
				//等级名称
				String levelName = cacheApplicationService.getDictName(middleLevel.getLevel());
				if (StringUtils.isEmpty(levelName)) {
					levelName = "其它等级";
				}
				
				SchoolExportLevelDto schoolExpLevelDto = mapLevelRatio.get(middleLevel.getLevel());
				if (schoolExpLevelDto == null) {
					schoolExpLevelDto = new SchoolExportLevelDto();
					schoolExpLevelDto.setLevelName(levelName);
					schoolExpLevelDto.setSxRatio(sxRatio);
					
					if (schoolChoiceId == null) {
						log.warn("中学代码："+middleSchoolCode+" 没有对应的雷达图信息。");
					}else {
						schoolExpLevelDto.setSchoolChoiceIds(schoolChoiceId.toString());
					}
				}else {
					Float currentSxRatio = schoolExpLevelDto.getSxRatio();
					
					schoolExpLevelDto.setSxRatio(Float.valueOf(CommonUtil.add(currentSxRatio.toString(), sxRatio.toString()).toString()));
					
					if (schoolChoiceId == null) {
						log.warn("中学代码："+middleSchoolCode+" 没有对应的雷达图信息。");
					}else {
						String currentChoiceId = schoolExpLevelDto.getSchoolChoiceIds();
						schoolExpLevelDto.setSchoolChoiceIds(currentChoiceId+"##"+schoolChoiceId.toString());
					}
				}
				mapLevelRatio.put(middleLevel.getLevel(), schoolExpLevelDto);
				
			}
		}
		
		List<SchoolExportLevelDto> ret = new ArrayList<SchoolExportLevelDto>();
		if (!mapLevelRatio.isEmpty()) {
			
			Set<Integer> levels = mapLevelRatio.keySet();
			for (Integer oneLevel : levels) {
				
				SchoolExportLevelDto oneSchoolExportLevelDto = mapLevelRatio.get(oneLevel);
				//处理升学占比的精度
				oneSchoolExportLevelDto.setSxRatio(Float.valueOf(CommonUtil.round(oneSchoolExportLevelDto.getSxRatio().toString(), 2)));
				ret.add(oneSchoolExportLevelDto);
			}
		}
		
		return ret;
	}


	/**
	 * 设置初中校在学校优选数据中对应的其它信息.
	 * @param schoolExportDtoes
	 */
	public void setSchoolChoiceInfo(String memberId, List<SchoolExportDto> schoolExportDtoes) {
		
		if (schoolExportDtoes != null && schoolExportDtoes.size() > 0) {
			for (SchoolExportDto oneSchoolExport :  schoolExportDtoes) {
				SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(Constant.DICT_ID_10003, oneSchoolExport.getSchoolCode());
				if (schoolChoice != null) {
					oneSchoolExport.setSchoolId(schoolChoice.getChoiceId());
					oneSchoolExport.setDimension(schoolChoice.getDimension());
					oneSchoolExport.setLongitude(schoolChoice.getLongitude());
					
					if (cacheApplicationService.checkSchoolIfCollected(memberId, Constant.DICT_ID_10003, oneSchoolExport.getSchoolId())) {
						oneSchoolExport.setColStatus(1);
					}else {
						oneSchoolExport.setColStatus(0);
					}
				}
				oneSchoolExport.setSchoolName(oneSchoolExport.getSchoolName()+oneSchoolExport.getCampusName());
				
			}
		}
	}

	/**
	 * 组装初中校出口数据._按每个出口初中校的占比 .
	 * @param middleSchoolExportes List<MiddleSchoolExport>.
	 * @return
	 */
	public List<SchoolExportDto> assembleMiddleSchoolExportData(List<MiddleSchoolExport> middleSchoolExportes) {
		
		List<SchoolExportDto> ret = new ArrayList<SchoolExportDto>();
		if (middleSchoolExportes != null && middleSchoolExportes.size() > 0) {
		    //占比小于或等于5%的 之和
			Float sumSxRatio = Float.valueOf("0.00");
			for (MiddleSchoolExport oneMiddleSchoolExport :  middleSchoolExportes) {
				// 升学占比
				Float currentRatio = oneMiddleSchoolExport.getSxRatio();
				//学校代码
				String currentSchoolCode = oneMiddleSchoolExport.getHighSchoolCode();
				//学校名称
				String currentSchoolName = oneMiddleSchoolExport.getHighSchoolName();
				//校区名称
				String currentCampusName = oneMiddleSchoolExport.getHighCampusName();
				
				if (currentRatio <= Float.valueOf("5.00")) {
					sumSxRatio = Float.valueOf(CommonUtil.add(currentRatio.toString(), sumSxRatio.toString()).toString());
					continue;
				}
				
				SchoolExportDto schoolExportDto = new SchoolExportDto();
				schoolExportDto.setSchoolCode(currentSchoolCode);
				schoolExportDto.setSchoolName(currentSchoolName+currentCampusName);
				schoolExportDto.setSxRatio(currentRatio);
				schoolExportDto.setOther(0);
				
				ret.add(schoolExportDto);
						
			}
			
			if (sumSxRatio > Float.valueOf("0.00")) {
				SchoolExportDto schoolExportDto = new SchoolExportDto();
				schoolExportDto.setSxRatio(Float.valueOf(CommonUtil.div(sumSxRatio.toString(), "1", 2)));
				schoolExportDto.setSchoolName("其他学校");
				schoolExportDto.setOther(1);
				ret.add(schoolExportDto);
			}
		}
		
		return ret;
	}

	/**
	 * 组装初中出口数据._按每个出口高中校等级 .
	 * @param middleSchoolExportes List<MiddleSchoolExport>.
	 * @return
	 */
	public List<SchoolExportLevelDto> assembleMiddleSchoolExportDataByLevel(
			List<MiddleSchoolExport> middleSchoolExportes) {
		
		Map<Integer, SchoolExportLevelDto> mapLevelRatio = new HashMap<Integer, SchoolExportLevelDto>();
		if (middleSchoolExportes != null && middleSchoolExportes.size() > 0) {
			for (MiddleSchoolExport oneMiddleSchoolExport : middleSchoolExportes) {
				//学校代码
				String highSchoolCode = oneMiddleSchoolExport.getHighSchoolCode();
				//升学占比
				Float sxRatio = oneMiddleSchoolExport.getSxRatio();
				
				//学校等级
				HighLevel highLevel = cacheApplicationService.getHighLevelBy(highSchoolCode);
				if (highLevel == null) {
					continue;
				}
				
				if (highLevel.getLevel() == null) {
					highLevel.setLevel(0);
				}
				//等级名称
				String levelName = cacheApplicationService.getDictName(highLevel.getLevel());
				if (StringUtils.isEmpty(levelName)) {
					levelName = "其它等级";
				}
				
				SchoolExportLevelDto schoolExpLevelDto = mapLevelRatio.get(highLevel.getLevel());
				if (schoolExpLevelDto == null) {
					schoolExpLevelDto = new SchoolExportLevelDto();
					schoolExpLevelDto.setLevelName(levelName);
					schoolExpLevelDto.setSxRatio(sxRatio);
					
				}else {
					Float currentSxRatio = schoolExpLevelDto.getSxRatio();
					
					schoolExpLevelDto.setSxRatio(Float.valueOf(CommonUtil.add(currentSxRatio.toString(), sxRatio.toString()).toString()));
					
				}
				mapLevelRatio.put(highLevel.getLevel(), schoolExpLevelDto);
				
			}
		}
		
		List<SchoolExportLevelDto> ret = new ArrayList<SchoolExportLevelDto>();
		if (!mapLevelRatio.isEmpty()) {
			
			Set<Integer> levels = mapLevelRatio.keySet();
			for (Integer oneLevel : levels) {
				
				SchoolExportLevelDto oneSchoolExportLevelDto = mapLevelRatio.get(oneLevel);
				//处理升学占比的精度
				oneSchoolExportLevelDto.setSxRatio(Float.valueOf(CommonUtil.round(oneSchoolExportLevelDto.getSxRatio().toString(), 2)));
				ret.add(oneSchoolExportLevelDto);
			}
		}
		
		return ret;
	}

	
	
	/**
	 * 获取学校出口树信息.
	 * @param serviceBlock  小学：10002  初中：10003
	 * @param schoolCode  学校代码.
	 * @param year  年度.
	 * @return SchoolExportDto.
	 */
	public SchoolExportDto getSchoolExportTreeData(Integer serviceBlock, String schoolCode, Integer year) {
		
		SchoolExportDto rootSchoolExport = new SchoolExportDto();
		//获取学校信息
		SchoolChoice currentSchoolInfo = cacheApplicationService.getSchoolChoiceBy(serviceBlock, schoolCode);
		if (currentSchoolInfo == null) {
			return rootSchoolExport;
		}
		rootSchoolExport.setSchoolName(currentSchoolInfo.getSchoolName());
		rootSchoolExport.setSchoolId(currentSchoolInfo.getChoiceId());
		rootSchoolExport.setSchoolCode(currentSchoolInfo.getSchoolCode());
		
		//小学
		if (serviceBlock.intValue() == Constant.DICT_ID_10002) {
			List<PrimSchoolExport> primSchoolExports = cacheApplicationService.getPrimSchoolExportBy(schoolCode, year);
			if (primSchoolExports != null && primSchoolExports.size() > 0) {
				List<SchoolExportDto> subSchoolExports = new ArrayList<SchoolExportDto>();
				for (PrimSchoolExport oneSchoolExport : primSchoolExports) {
					SchoolExportDto tempSchoolExport = new SchoolExportDto();
					tempSchoolExport.setSchoolName(oneSchoolExport.getMiddleSchoolName()+oneSchoolExport.getMiddleCampusName());
					tempSchoolExport.setSxRatio(oneSchoolExport.getSxRatio());
					
					Integer schoolId = cacheApplicationService.getSchoolChoiceIdBy(Constant.DICT_ID_10003, schoolCode);
					tempSchoolExport.setSchoolId(schoolId);

					List<MiddleSchoolExport> middleSchoolExports = cacheApplicationService.getMiddleSchoolExportBy(oneSchoolExport.getMiddleSchoolCode(), year);
					if (middleSchoolExports != null && middleSchoolExports.size() > 0) {
						
						List<SchoolExportDto> sub2SchoolExports = new ArrayList<SchoolExportDto>();
						for (MiddleSchoolExport oneMiddleSchoolExport : middleSchoolExports) {
							SchoolExportDto temp2SchoolExport = new SchoolExportDto();
							temp2SchoolExport.setSchoolName(oneMiddleSchoolExport.getHighSchoolName());
							temp2SchoolExport.setSxRatio(oneMiddleSchoolExport.getSxRatio());
							sub2SchoolExports.add(temp2SchoolExport);
						}
						//按升学占比排序
						Collections.sort(sub2SchoolExports, new SchoolExportDtoComparator());
						tempSchoolExport.setSchoolExportDtoes(sub2SchoolExports);
					}
					subSchoolExports.add(tempSchoolExport);
				}
				//按升学占比排序
				Collections.sort(subSchoolExports, new SchoolExportDtoComparator());
				rootSchoolExport.setSchoolExportDtoes(subSchoolExports);
			}
		}
		//初中
		if (serviceBlock.intValue() == Constant.DICT_ID_10003) {
			List<MiddleSchoolExport> middleSchoolExports = cacheApplicationService.getMiddleSchoolExportBy(schoolCode, year);
			if (middleSchoolExports != null && middleSchoolExports.size() > 0) {
				
				List<SchoolExportDto> sub2SchoolExports = new ArrayList<SchoolExportDto>();
				for (MiddleSchoolExport oneMiddleSchoolExport : middleSchoolExports) {
					SchoolExportDto temp2SchoolExport = new SchoolExportDto();
					temp2SchoolExport.setSchoolName(oneMiddleSchoolExport.getHighSchoolName());
					temp2SchoolExport.setSxRatio(oneMiddleSchoolExport.getSxRatio());
					sub2SchoolExports.add(temp2SchoolExport);
				}
				rootSchoolExport.setSchoolExportDtoes(sub2SchoolExports);
			}
		}
		
		return rootSchoolExport;
	}

}
