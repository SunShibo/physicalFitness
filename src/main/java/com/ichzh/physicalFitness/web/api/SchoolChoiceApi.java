package com.ichzh.physicalFitness.web.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.SchoolChoiceRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.SchoolChoiceService;
import com.ichzh.physicalFitness.service.SchoolDetailQueryRecordService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/schoolchoice"})
public class SchoolChoiceApi {
	
	@Autowired
    SchoolChoiceRepository schoolChoiceRepository;
	@Autowired
    SchoolChoiceService schoolChoiceService;
	@Autowired
    UserService userService;
	@Autowired
	SchoolDetailQueryRecordService schoolDetailQueryRecordService;
	
	/**
	 * 根据中小学名称查询中小学信息
	 * @param schoolName  学校名称
	 * @param town 学校所属区   该参数为了缴费拦截用
	 * @param serviceBlock  服务模块   该参数为了缴费拦截用
	 * @return
	 */
	@RequestMapping(value="/querySchoolChoice", method= {RequestMethod.POST})
	public OperaResult querySchoolChoice(
			@RequestParam(value = "schoolName", required = true) String schoolName,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock
			) {
		OperaResult result = new OperaResult();
		try
		{
			List<Object> schoolChoices = schoolChoiceRepository.querySchoolChoinceBy(schoolName, town);
			result.setResultCode(OperaResult.Success);
			result.setData(schoolChoices);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 根据学校ID查询中小学详情信息
	 * @param chioceId 中小学标识号 
	 * @return
	 */
	@RequestMapping(value="/getSchoolChoiceById", method= {RequestMethod.POST})
	public OperaResult getSchoolChoiceById(
			@RequestParam(value = "chioceId", required = true) Integer chioceId,HttpServletRequest request) {
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			SchoolChoice school = schoolChoiceRepository.findOne(chioceId);
			//设置 5个雷达图评价描述
			schoolChoiceService.setOtherData(memberId, school);
			//设置学校标签
			schoolChoiceService.setSchoolLabel(school);
			//设置办学类型的名称和描述
			schoolChoiceService.setSchoolRunningTypeData(school);
			//设置哪些模块是否需要显示
			schoolChoiceService.setModelIfShow(school);
			//将历史沿革拆分为多行数据集合
			schoolChoiceService.setHistoryEvolution(school);
			//设置热力分析数据
			schoolChoiceService.setHeatAnalysises(school);
			//设置连年趋势数据
			schoolChoiceService.setTrend(school);
			//设置排名数据(按区)
			schoolChoiceService.setRanking(school);
			//设置排名数据(按片区)
			schoolChoiceService.setRankingArea(school);
			
			if (school != null) {
				schoolDetailQueryRecordService.executeAterSchoolDetailQuerySuccess(memberId, school.getSchoolCode(), 
						2, school.getSchoolName());
			}
			result.setResultCode(OperaResult.Success);
			result.setData(school);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
}
