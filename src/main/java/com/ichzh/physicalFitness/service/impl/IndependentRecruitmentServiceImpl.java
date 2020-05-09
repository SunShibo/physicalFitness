package com.ichzh.physicalFitness.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.IndependentRecruitment;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.IndependentRecruitmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("independentRecruitmentService")
public class IndependentRecruitmentServiceImpl implements IndependentRecruitmentService {

	@Autowired
	ICacheApplicationService cacheApplicationService;
	/**
	 * 为招生范围中的学校设置其它信息.
	 * @param independentRecruitments  List<IndependentRecruitment>
	 * @param memberId  当前登录会员标识号.
	 */
	public void setOtherData(List<IndependentRecruitment> independentRecruitments, String memberId) {
		
		if (independentRecruitments != null && independentRecruitments.size() > 0 && StringUtils.isNotEmpty(memberId)) {
			for (IndependentRecruitment oneIndependentRecruitment : independentRecruitments) {
				//学校代码
				String schoolCode = oneIndependentRecruitment.getSchoolCode();
				//学校类型
				Integer serviceBlock = oneIndependentRecruitment.getServiceBlock();
				SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(serviceBlock, schoolCode);
				if (schoolChoice != null) {
					oneIndependentRecruitment.setChoiceId(schoolChoice.getChoiceId());
					oneIndependentRecruitment.setSchoolId(schoolChoice.getChoiceId());
					oneIndependentRecruitment.setLongitude(schoolChoice.getLongitude());
					oneIndependentRecruitment.setDimension(schoolChoice.getDimension());
					
					boolean ifCollected = cacheApplicationService.checkSchoolIfCollected(memberId, serviceBlock, schoolChoice.getChoiceId());
					if (ifCollected) {
						oneIndependentRecruitment.setColStatus(1);
					}else {
						oneIndependentRecruitment.setColStatus(0);
					}
				}else {
					log.warn("根据学段和学校代码没有获取的雷达数据，学段为："+serviceBlock+" || 学籍代码为："+schoolCode);
				}
			}
		}
	}

	
	
}
