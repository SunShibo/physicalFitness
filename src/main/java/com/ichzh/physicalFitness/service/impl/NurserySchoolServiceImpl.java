package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.model.SchoolLabel;
import com.ichzh.physicalFitness.model.SysDict;
import com.ichzh.physicalFitness.repository.SchoolLabelRespository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.NurserySchoolService;

@Service("nurserySchoolService")
public class NurserySchoolServiceImpl implements NurserySchoolService {

	@Autowired
	private SchoolLabelRespository schoolLabelRespository;
	@Autowired
	private ICacheApplicationService cacheApplicationService;
	/**
	 * 将学校简介的五类信息拆分为多个段落
	 * @param nurserySchool
	 */
	public void splitSchoolProfile(NurserySchool nurserySchool) {
		
		//学校历史
		String schoolHistory = nurserySchool.getSchoolHistory();
		if (StringUtils.isNotEmpty(schoolHistory)) {
			nurserySchool.setListSchoolHistory(Arrays.asList(schoolHistory.split("##")));
		}
		
		//学校位置
		String schoolAddress = nurserySchool.getSchoolAddress();
		if (StringUtils.isNotEmpty(schoolAddress)) {
			nurserySchool.setListSchoolAddress(Arrays.asList(schoolAddress.split("##")));
		}
		
		//办学理念
		String runningIdea = nurserySchool.getRunningIdea();
		if (StringUtils.isNotEmpty(runningIdea)) {
			nurserySchool.setListRunningIdea(Arrays.asList(runningIdea.split("##")));
		}		
		
		//学校规模
		String schoolSize = nurserySchool.getSchoolSize();
		if (StringUtils.isNotEmpty(schoolSize)) {
			nurserySchool.setListSchoolSize(Arrays.asList(schoolSize.split("##")));
		}
		
		//学校特色
		String schoolCharacter = nurserySchool.getSchoolCharacter();
		if (StringUtils.isNotEmpty(schoolCharacter)) {
			nurserySchool.setListSchoolCharacter(Arrays.asList(schoolCharacter.split("##")));
		}		
		
	}

	/**
	 * 设置学校标签
	 * @param schoolChoice
	 */
	public void setSchoolLabel(NurserySchool nurserySchool) {
		
		List<SchoolLabel> schoolLables = schoolLabelRespository.querySchoolLabelBy(nurserySchool.getSchoolCode());
		nurserySchool.setSchoolLabeles(schoolLables);
	}

	/**
	 * 设置收藏状态
	 * @param memberId
	 * @param nurserySchool
	 */
	public void setColStatus(String memberId, NurserySchool nurserySchool) {
		
		if (cacheApplicationService.checkSchoolIfCollected(memberId, Constant.DICT_ID_10001, nurserySchool.getNurserySchoolId())) {
			nurserySchool.setColStatus(1);
		}else {
			nurserySchool.setColStatus(0);
		}
	}

	/**
	  * 设置举办者名称和描述
	 * @param nurserySchool
	 */
	public void setSchoolRunningTypeData(NurserySchool nurserySchool) {
		
		if (nurserySchool != null) {
			SysDict schoolRunningTypeDict = cacheApplicationService.getDictByDictId(nurserySchool.getOrganizers());
			nurserySchool.setOrganizersName(schoolRunningTypeDict.getDictName());
			nurserySchool.setOrganizersDesc(schoolRunningTypeDict.getDictCode());
		}
	}
	
	

	
}
