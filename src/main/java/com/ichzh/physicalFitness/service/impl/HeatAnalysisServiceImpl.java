package com.ichzh.physicalFitness.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.HeatAnalysis;
import com.ichzh.physicalFitness.repository.HeatAnalysisRepository;
import com.ichzh.physicalFitness.service.HeatAnalysisService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.util.HeatingPowerLevelUtil;

@Service("heatAnalysisService")
public class HeatAnalysisServiceImpl implements HeatAnalysisService {

	@Autowired
	HeatAnalysisRepository heatAnalysisRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	/**
	 * 查询某个学校最新年份的热力分析
	 * @param serviceBlock
	 * @param schoolCode
	 * @return
	 */
	public List<HeatAnalysis> queryHeatAnalysisBy(Integer serviceBlock, String schoolCode) {
		List<HeatAnalysis> ret = new ArrayList<HeatAnalysis>();
		List<HeatAnalysis>  heatAnalysiss = heatAnalysisRepository.queryHeatAnalysisBy(serviceBlock, schoolCode);
		if (heatAnalysiss != null && heatAnalysiss.size() > 0) {
			//最新的年是哪一年
			Integer yearYear = heatAnalysiss.get(0).getYearYear();
			for (HeatAnalysis oneHeatAnalysis : heatAnalysiss) {
				if (oneHeatAnalysis.getYearYear().compareTo(yearYear) == 0) {
					this.setOtherData(oneHeatAnalysis);
					ret.add(oneHeatAnalysis);
				}
			}
		}
		return ret;
	}
	
	/**
	 * 设置占比等级和入学方式名称
	 * @param heatAnalysis
	 */
	public void setOtherData(HeatAnalysis heatAnalysis) {
		
		//占比
		BigDecimal radio = heatAnalysis.getRatio();
		//入学方式
		Integer admissionMode = heatAnalysis.getAdmissionMode();
		
		heatAnalysis.setAdmissionModeName(cacheApplicationService.getDictName(admissionMode));
		heatAnalysis.setRadioLevel(HeatingPowerLevelUtil.getHeatingValueLevel2(radio));
		
	}
	
	

	
}
