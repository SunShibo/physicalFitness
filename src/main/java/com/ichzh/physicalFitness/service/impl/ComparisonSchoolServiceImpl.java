package com.ichzh.physicalFitness.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.ComparisonSchool;
import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.ComparisonSchoolRepository;
import com.ichzh.physicalFitness.service.ComparisonSchoolService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.SchoolChoiceService;

import lombok.extern.slf4j.Slf4j;

@Service("comparisonSchoolService")
@Slf4j
public class ComparisonSchoolServiceImpl implements ComparisonSchoolService {
	
	@Autowired
	ComparisonSchoolRepository comparisonSchoolRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	@Autowired
	SchoolChoiceService schoolChoiceService;

	public boolean writeComSchool(String memberId, Integer schoolId, Integer schoolType, Integer orderNum) {
		
		ComparisonSchool comparisonSchool = new ComparisonSchool();
		List<ComparisonSchool> comparisonSchools = comparisonSchoolRepository.findByMemberIdAndSchoolTypeAndSchoolId(memberId, schoolType, schoolId);
		if (comparisonSchools != null && comparisonSchools.size() > 0) {
			comparisonSchool = comparisonSchools.get(0);
			if (comparisonSchools.size() > 1) {
				log.warn("同一个学校被同一个会员加入了两次，memberId="+memberId+" || schoolType="+schoolType.toString() + " || schoolId="+schoolId);
			}
		}
		
		comparisonSchool.setMemberId(memberId);
		comparisonSchool.setSchoolId(schoolId);
		comparisonSchool.setSchoolType(schoolType);
		comparisonSchool.setOrderNum(orderNum);
		
		comparisonSchoolRepository.saveAndFlush(comparisonSchool);
		// 将当前会员收藏学校的显示序号增加1（当前学校除外）
		comparisonSchoolRepository.updateOtherOrderNum(memberId, schoolId);
		
		return true;
	}

	/**
	 * 设置对比校的热力值
	 * @param ComparisonSchools List<ComparisonSchool>.
	 */
	public void setSchoolHeatingPowerValue(List<ComparisonSchool> comparisonSchools) {
		
		if (comparisonSchools != null && comparisonSchools.size() > 0 ) {
			for (ComparisonSchool oneComparisonSchool:comparisonSchools) {
				//学校类型
				Integer schoolType = oneComparisonSchool.getSchoolType();
				//学校ID
				Integer schoolId = oneComparisonSchool.getSchoolId();
				
				oneComparisonSchool.setChoiceId(schoolId);
				// 中小学
				if (schoolType.intValue() == 1) {
					SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(schoolId);
					schoolChoiceService.setSchoolHeatingPowerValue(schoolChoice);
					
					oneComparisonSchool.setHeatingValue4Show(schoolChoice.getHeatingValue4Show());
					
					oneComparisonSchool.setSchoolDevelopment(schoolChoice.getSchoolDevelopment());
					oneComparisonSchool.setStudentSource(schoolChoice.getStudentSource());
					oneComparisonSchool.setFacultyStrength(schoolChoice.getFacultyStrength());
					oneComparisonSchool.setFamousTeacherRes(schoolChoice.getFamousTeacherRes());
					oneComparisonSchool.setHardwareCondition(schoolChoice.getHardwareCondition());
					
					
					oneComparisonSchool.setSchoolName(schoolChoice.getSchoolName());
					oneComparisonSchool.setSchoolCode(schoolChoice.getSchoolCode());
					oneComparisonSchool.setServiceBlock(schoolChoice.getServiceBlock());
					
				}else {
					NurserySchool nurserySchool = cacheApplicationService.getNurserySchoolBy(schoolId);
					
					oneComparisonSchool.setCampusDevelopment(nurserySchool.getCampusDevelopment());
					oneComparisonSchool.setCampusScale(nurserySchool.getCampusScale());
					oneComparisonSchool.setFacultyStrength(nurserySchool.getFacultyStrength());
					oneComparisonSchool.setHardwareCondition(nurserySchool.getHardwareCondition());
					
					oneComparisonSchool.setSchoolName(nurserySchool.getSchoolName());
					oneComparisonSchool.setSchoolCode(nurserySchool.getSchoolCode());
					oneComparisonSchool.setServiceBlock(Constant.DICT_ID_10001);
				}
			}
		}
	}


	
	
	
	

}
