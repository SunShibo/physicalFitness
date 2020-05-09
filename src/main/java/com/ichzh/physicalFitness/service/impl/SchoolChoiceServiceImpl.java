package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.RankingArea;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.model.SchoolHeatingPower;
import com.ichzh.physicalFitness.model.SchoolLabel;
import com.ichzh.physicalFitness.model.SysDict;
import com.ichzh.physicalFitness.repository.SchoolLabelRespository;
import com.ichzh.physicalFitness.service.HeatAnalysisService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.RankingAreaService;
import com.ichzh.physicalFitness.service.RankingService;
import com.ichzh.physicalFitness.service.SchoolChoiceService;
import com.ichzh.physicalFitness.service.TrendService;
import com.ichzh.physicalFitness.util.RadarChartContantUtil;
import com.ichzh.physicalFitness.util.SchoolDetailModelShowUtil;

@Service("schoolChoiceService")
public class SchoolChoiceServiceImpl implements SchoolChoiceService {

	
	@Autowired
	private ICacheApplicationService cacheApplicationService;
	@Autowired
	private SchoolLabelRespository schoolLabelRespository;
	@Autowired
	private HeatAnalysisService heatAnalysisService;
	@Autowired
	private TrendService trendService;
	@Autowired
	private RankingService rankingService;
	@Autowired
	private RankingAreaService rankingAreaService;
	
	public void setOtherData(String memberId, SchoolChoice schoolChoice) {
		
		//学校发展
		schoolChoice.setSchoolDevelopmentDesc(RadarChartContantUtil.
				getEvaluationDesc(RadarChartContantUtil.SCHOOL_DEVELOP, schoolChoice.getSchoolDevelopment()));
		//生源情况
		schoolChoice.setStudentSourceDesc(RadarChartContantUtil.
				getEvaluationDesc(RadarChartContantUtil.STUDENT_SOURCE_CASE, schoolChoice.getStudentSource()));
		// 师资力量
		schoolChoice.setFacultyStrengthDesc(RadarChartContantUtil.
				getEvaluationDesc(RadarChartContantUtil.FACULTY_STRENGTH, schoolChoice.getFacultyStrength()));
		
		// 名师资源
		schoolChoice.setFamousTeacherResDesc(RadarChartContantUtil.
				getEvaluationDesc(RadarChartContantUtil.FAMOUS_TEACHER_RESOURCE, schoolChoice.getFamousTeacherRes()));
		// 硬件条件
		schoolChoice.setHardwareConditionDesc(RadarChartContantUtil.
				getEvaluationDesc(RadarChartContantUtil.HARDWARE_CONDITION, schoolChoice.getHardwareCondition()));
		
		//设置学校是否被收藏
		if (cacheApplicationService.checkSchoolIfCollected(memberId, schoolChoice.getServiceBlock(), schoolChoice.getChoiceId())) {
			schoolChoice.setColStatus(1);
		}else {
			schoolChoice.setColStatus(0);
		}
		
	}

	/**
	 * 将学校简介的五类信息拆分为多个段落
	 * @param nurserySchool
	 */
	public void splitSchoolProfile(SchoolChoice schoolChoice) {
		
		
		//学校历史
		String schoolHistory = schoolChoice.getSchoolHistory();
		if (StringUtils.isNotEmpty(schoolHistory)) {
			schoolChoice.setListSchoolHistory(Arrays.asList(schoolHistory.split("##")));
		}
		
		//学校位置
		String schoolAddress = schoolChoice.getSchoolAddress();
		if (StringUtils.isNotEmpty(schoolAddress)) {
			schoolChoice.setListSchoolAddress(Arrays.asList(schoolAddress.split("##")));
		}
		
		//办学理念
		String runningIdea = schoolChoice.getRunningIdea();
		if (StringUtils.isNotEmpty(runningIdea)) {
			schoolChoice.setListRunningIdea(Arrays.asList(runningIdea.split("##")));
		}		
		
		//学校规模
		String schoolSize = schoolChoice.getSchoolSize();
		if (StringUtils.isNotEmpty(schoolSize)) {
			schoolChoice.setListSchoolSize(Arrays.asList(schoolSize.split("##")));
		}
		
		//学校特色
		String schoolCharacter = schoolChoice.getSchoolCharacter();
		if (StringUtils.isNotEmpty(schoolCharacter)) {
			schoolChoice.setListSchoolCharacter(Arrays.asList(schoolCharacter.split("##")));
		}				
		
	}

	/**
	 * 设置学校对应的热力值
	 * @param schoolChoice
	 */
	public void setSchoolHeatingPowerValue(SchoolChoice schoolChoice) {
		
		//学校对应的热力值
		Float heatingPowerValue = cacheApplicationService.getHeatingPowerValueBy(schoolChoice.getSchoolCode(), schoolChoice.getYearYear());
		if (heatingPowerValue != null) {
			schoolChoice.setHeatingValue(heatingPowerValue);
			if (heatingPowerValue.compareTo(Float.valueOf("1")) == 0) {
				schoolChoice.setHeatingValue4Show(Float.valueOf("90"));
			}
			if (heatingPowerValue.compareTo(Float.valueOf("2")) == 0) {
				schoolChoice.setHeatingValue4Show(Float.valueOf("70"));
			}
			if (heatingPowerValue.compareTo(Float.valueOf("3")) == 0) {
				schoolChoice.setHeatingValue4Show(Float.valueOf("50"));
			}
			if (heatingPowerValue.compareTo(Float.valueOf("4")) == 0) {
				schoolChoice.setHeatingValue4Show(Float.valueOf("30"));
			}
			if (heatingPowerValue.compareTo(Float.valueOf("5")) == 0) {
				schoolChoice.setHeatingValue4Show(Float.valueOf("10"));
			}
			
		}
	}

	/**
	 * 设置当前学校所在区，每个热力等级对应的学校
	 * @param schoolChoice
	 */
	public void setLevelSchoolHeatingPower(SchoolChoice schoolChoice, String memberId) {
		
		//当前学校所在区第一等级的学校
		List<SchoolHeatingPower> levelones = cacheApplicationService.getHeatingPowerInfoBy(schoolChoice.getTown(), 2, 1, memberId);
		//当前学校所在区第二等级的学校
		List<SchoolHeatingPower> leveltwos = cacheApplicationService.getHeatingPowerInfoBy(schoolChoice.getTown(), 2, 2, memberId);
		//当前学校所在区第三等级的学校
		List<SchoolHeatingPower> levelthrees = cacheApplicationService.getHeatingPowerInfoBy(schoolChoice.getTown(), 2, 3, memberId);
		//当前学校所在区第四等级的学校
		List<SchoolHeatingPower> levelfours = cacheApplicationService.getHeatingPowerInfoBy(schoolChoice.getTown(), 2, 4, memberId);
		//当前学校所在区第五等级的学校
		List<SchoolHeatingPower> levelfives = cacheApplicationService.getHeatingPowerInfoBy(schoolChoice.getTown(), 2, 5, memberId);
		
		schoolChoice.setLevelones(levelones);
		schoolChoice.setLeveltwos(leveltwos);
		schoolChoice.setLevelthrees(levelthrees);
		schoolChoice.setLevelfours(levelfours);
		schoolChoice.setLevelfives(levelfives);
		
		
	}

	/**
	 * 设置学校标签
	 * @param schoolChoice
	 */
	public void setSchoolLabel(SchoolChoice schoolChoice) {
		
		List<SchoolLabel> schoolLables = schoolLabelRespository.querySchoolLabelBy(schoolChoice.getSchoolCode());
		schoolChoice.setSchoolLabeles(schoolLables);
	}

	/**
	 * 设置学校对应的热力值
	 * @param schoolChoices
	 */
	public void setSchoolHeatingPowerValue(List<SchoolChoice> schoolChoices) {
		if (schoolChoices != null && schoolChoices.size() >0) {
			for (SchoolChoice oneScholChoice : schoolChoices) {
				this.setSchoolHeatingPowerValue(oneScholChoice);
			}
		}
		
	}

	/**
	  * 设置办学类型名称和描述
	 * @param schoolChoice
	 */
	public void setSchoolRunningTypeData(SchoolChoice schoolChoice) {
		
		if (schoolChoice != null) {
			SysDict schoolRunningTypeDict = cacheApplicationService.getDictByDictId(schoolChoice.getSchoolRunningType());
			schoolChoice.setSchoolRunningTypeName(schoolRunningTypeDict.getDictName());
			schoolChoice.setSchoolRuningTypeDesc(schoolRunningTypeDict.getDictCode());
		}
	}

	/**
	 * 根据具体数据情况，设置哪些模块是否需要显示.
	 * @param schoolChoice
	 */
	public void setModelIfShow(SchoolChoice schoolChoice) {
		
		if (schoolChoice != null) {
			schoolChoice.setSchoolHistoryIfShow(SchoolDetailModelShowUtil.schoolHistoryIfShow(schoolChoice));
			schoolChoice.setTeacherCaseIfShow(SchoolDetailModelShowUtil.teacherCaseIfShow(schoolChoice));
			schoolChoice.setStudentCaseIfShow(SchoolDetailModelShowUtil.studentCaseIfShow(schoolChoice));
			schoolChoice.setHardwareConditionIfShow(SchoolDetailModelShowUtil.hardwareConditionIfShow(schoolChoice));
		}
	}

	/**
	 * 将历史沿革拆分为多条数据集合
	 * @param schoolChoice
	 */
	public void setHistoryEvolution(SchoolChoice schoolChoice) {
		
		List<Map<String, String>> ret = new ArrayList<Map<String,String>>();
		if (schoolChoice != null) {
			//历史沿革
			String historicalEvolution = schoolChoice.getHistoricalEvolution();
			if (StringUtils.isNotEmpty(historicalEvolution)) {
				String[] arraHisoricalEvo = historicalEvolution.split("####");
				Map<String, String> mapTemp = new HashMap<String, String>();
				mapTemp.put("year", arraHisoricalEvo[0]);
				mapTemp.put("historyDesc", arraHisoricalEvo[1]);
				
				ret.add(mapTemp);
			}
		}
		if (ret.size() > 0) {
			schoolChoice.setHistoryEvolutions(ret);
		}
	}

	/**
	 * 设置热力分析数据
	 * @param schoolChoice
	 */
	public void setHeatAnalysises(SchoolChoice schoolChoice) {
		
		schoolChoice.setHeatAnalysises(heatAnalysisService.queryHeatAnalysisBy(schoolChoice.getServiceBlock(), schoolChoice.getSchoolCode()));
	}

	/**
	 * 设置趋势值
	 * @param schoolChoice
	 */
	public void setTrend(SchoolChoice schoolChoice) {
		
		schoolChoice.setTrends(trendService.queryTrendBy(schoolChoice.getServiceBlock(), schoolChoice.getSchoolCode()));
	}

	/**
	 * 设置排名(按区)
	 * @param schoolChoice
	 */
	public void setRanking(SchoolChoice schoolChoice) {
		schoolChoice.setRanking(rankingService.getTop5RankingAndSelfSortNum(schoolChoice.getServiceBlock(), schoolChoice.getSchoolCode(), schoolChoice.getTown()));
	}

	/**
	 * 设置排名(按片区)
	 * @param schoolChoice
	 */
	public void setRankingArea(SchoolChoice schoolChoice) {
		
		schoolChoice.setRankingArea(rankingAreaService.getTop5RankingAndSelfSortNum(schoolChoice.getServiceBlock(), schoolChoice.getSchoolCode()));
	}
	
	
	
	
	
	
	
	
	
}
