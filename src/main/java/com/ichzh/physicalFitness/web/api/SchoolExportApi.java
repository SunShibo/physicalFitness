package com.ichzh.physicalFitness.web.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.dto.SchoolExportDto;
import com.ichzh.physicalFitness.dto.SchoolExportLevelDto;
import com.ichzh.physicalFitness.model.MiddleSchoolExport;
import com.ichzh.physicalFitness.model.PrimSchoolExport;
import com.ichzh.physicalFitness.repository.MiddleSchoolExportRepository;
import com.ichzh.physicalFitness.repository.PrimSchoolExportRepository;
import com.ichzh.physicalFitness.service.PrimSchoolExportQueryRecordService;
import com.ichzh.physicalFitness.service.SchoolExportService;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.util.SchoolExportDtoComparator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/schoolExport"})
public class SchoolExportApi {
	
	@Autowired
	PrimSchoolExportRepository primSchoolExportRepository;
	@Autowired
	MiddleSchoolExportRepository middleSchoolExportRepository;
	@Autowired
	SchoolExportService schoolExportService;
	@Autowired
    UserService userService;
	@Autowired
	PrimSchoolExportQueryRecordService primSchoolExportQueryRecordService;

	/**
	  * 查询小学出口
	 * @param serviceBlock  服务模块(后台收费拦截用)
	 * @param town          入学区域 (后台收费拦截用) 
	 * @param schoolCode    小学学校代码
	 * @return
	 */
	@RequestMapping(value="/queryPrimSchoolExport", method= {RequestMethod.POST})
	public OperaResult queryPrimSchoolExportBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "year", required = true) Integer year, 
			@RequestParam(value = "schoolCode", required = true) String schoolCode) {
		
		  OperaResult result = new OperaResult();
		  List<PrimSchoolExport> primSchoolExports = primSchoolExportRepository.findByPrimSchoolCodeAndYear(schoolCode, year);
		  List<SchoolExportDto> assemblePrimSchoolExportData = schoolExportService.assemblePrimSchoolExportData(primSchoolExports);
		  Collections.sort(assemblePrimSchoolExportData, new SchoolExportDtoComparator());
		  //计算每个等级的占比
		  List<SchoolExportLevelDto> schoolExportLevels = schoolExportService.assemblePrimSchoolExportDataByLevel(primSchoolExports); 
		  Map<String, Object> schoolExportMap = new HashMap<String, Object>();
		  //按出口校
		  schoolExportMap.put("bySchool", assemblePrimSchoolExportData);
		  //按出口校等级
		  schoolExportMap.put("bySchoolLevel", schoolExportLevels);
		  
		  result.setResultCode(OperaResult.Success);
		  result.setData(schoolExportMap);
		  
		  return result;
	}
	/**
	 * 查询小学出口——最近三年是那几年
	 * @param serviceBlock 服务模块(后台收费拦截用)
	 * @param town         入学区域 (后台收费拦截用) 
	 * @param schoolCode   小学学校代码
	 * @return
	 */
	@RequestMapping(value="/queryYearPrimSchoolExport", method= {RequestMethod.POST})
	public OperaResult queryYearPrimSchoolExport(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolCode", required = true) String schoolCode) {
		OperaResult result = new OperaResult();
		List<Integer> queryYearOfPrimSchoolExportLatestThree = primSchoolExportRepository.queryYearOfPrimSchoolExportLatestThree(schoolCode);
		result.setResultCode(OperaResult.Success);
		result.setData(queryYearOfPrimSchoolExportLatestThree);
		           
		return result;
	}
	
	/**
	 * 查询小学出口——占比在5%以下的初中校列表
	 * @param serviceBlock 服务模块(后台收费拦截用)
	 * @param town         入学区域 (后台收费拦截用) 
	 * @param schoolCode   小学学校代码
	 * @param year 出口数据所属年
	 * @return
	 */
	@RequestMapping(value="/queryMiddleSchoooListData", method= {RequestMethod.POST})
	public OperaResult queryMiddleSchoooListData(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolCode", required = true) String schoolCode,
			@RequestParam(value = "year", required = true) Integer year,HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		String memberId = userService.getCurrentLoginUserId(request);
		if (StringUtils.isEmpty(memberId)) {
			result.setResultCode(OperaResult.Error);
			return result;
		}
		
		List<SchoolExportDto> queryOtherExportSchoolListData = primSchoolExportRepository.queryOtherExportSchoolListData(schoolCode, year);
		schoolExportService.setSchoolChoiceInfo(memberId, queryOtherExportSchoolListData);
		
		result.setResultCode(OperaResult.Success);
		result.setData(queryOtherExportSchoolListData);
		return result;
	}
	
	
	/**
	  * 查询中学出口
	 * @param serviceBlock  服务模块(后台收费拦截用)
	 * @param town          入学区域 (后台收费拦截用) 
	 * @param schoolCode    初中学校代码
	 * @return
	 */
	@RequestMapping(value="/queryMiddleSchoolExport", method= {RequestMethod.POST})
	public OperaResult queryMiddleSchoolExportBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "year", required = true) Integer year, 
			@RequestParam(value = "schoolCode", required = true) String schoolCode) {
		
		  OperaResult result = new OperaResult();
		  List<MiddleSchoolExport> middleSchoolExports = middleSchoolExportRepository.findByMiddleSchoolCodeAndYear(schoolCode, year);
		  List<SchoolExportDto> assemblePrimSchoolExportData = schoolExportService.assembleMiddleSchoolExportData(middleSchoolExports);
		  Collections.sort(assemblePrimSchoolExportData, new SchoolExportDtoComparator());
		  //计算每个等级的占比
		  List<SchoolExportLevelDto> schoolExportLevels = schoolExportService.assembleMiddleSchoolExportDataByLevel(middleSchoolExports); 
		  Map<String, Object> schoolExportMap = new HashMap<String, Object>();
		  //按出口校
		  schoolExportMap.put("bySchool", assemblePrimSchoolExportData);
		  //按出口校等级
		  schoolExportMap.put("bySchoolLevel", schoolExportLevels);
		  
		  result.setResultCode(OperaResult.Success);
		  result.setData(schoolExportMap);
		  
		  return result;
	}
	
	/**
	 * 查询初中出口——最近三年是那几年
	 * @param serviceBlock 服务模块(后台收费拦截用)
	 * @param town         入学区域 (后台收费拦截用) 
	 * @param schoolCode   初中学校代码
	 * @return
	 */
	@RequestMapping(value="/queryYearMiddleSchoolExport", method= {RequestMethod.POST})
	public OperaResult queryYearMiddleSchoolExport(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolCode", required = true) String schoolCode) {
		OperaResult result = new OperaResult();
		List<Integer> queryYearOfMiddleSchoolExportLatestThree = middleSchoolExportRepository.queryYearOfMiddleSchoolExportLatestThree(schoolCode);
		result.setResultCode(OperaResult.Success);
		result.setData(queryYearOfMiddleSchoolExportLatestThree);
		           
		return result;
	}
	
	
	/**
	 * 查询初中出口——占比在5%以下的高中校列表
	 * @param serviceBlock 服务模块(后台收费拦截用)
	 * @param town         入学区域 (后台收费拦截用) 
	 * @param schoolCode   初中学校代码
	 * @param year 出口数据所属年
	 * @return
	 */
	@RequestMapping(value="/queryHighSchoooListData", method= {RequestMethod.POST})
	public OperaResult queryHighSchoooListData(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolCode", required = true) String schoolCode,
			@RequestParam(value = "year", required = true) Integer year) {
		OperaResult result = new OperaResult();
		List<SchoolExportDto> queryOtherExportSchoolListData = middleSchoolExportRepository.queryOtherExportSchoolListData(schoolCode, year);
		
		result.setResultCode(OperaResult.Success);
		result.setData(queryOtherExportSchoolListData);
		return result;
	}
	
	
	/**
	  * 查询出口树形数据
	 * @param serviceBlock  学段: 10002  小学  10003 初中   
	 * @param town          入学区域 
	 * @param schoolCode    学校代码
	 * @return
	 */
	@RequestMapping(value="/querySchoolExportTree", method= {RequestMethod.POST})
	public OperaResult querySchoolExportTreeBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "year", required = true) Integer year,
			@RequestParam(value = "schoolCode", required = true) String schoolCode,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			SchoolExportDto schoolExportTreeData = schoolExportService.getSchoolExportTreeData(serviceBlock, schoolCode, year);
			result.setResultCode(OperaResult.Success);
			result.setData(schoolExportTreeData);
			
			if (schoolExportTreeData != null) {
				primSchoolExportQueryRecordService.executeAterSchoolDetailQuerySuccess(memberId, schoolExportTreeData.getSchoolCode(), 
						2, schoolExportTreeData.getSchoolName());
			}
			
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return result;
	}
}
