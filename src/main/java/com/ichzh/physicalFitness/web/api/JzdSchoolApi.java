package com.ichzh.physicalFitness.web.api;

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

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.dto.RangeDto;
import com.ichzh.physicalFitness.dto.SchoolDto;
import com.ichzh.physicalFitness.dto.StreetCommunityDto;
import com.ichzh.physicalFitness.model.JzdSchool;
import com.ichzh.physicalFitness.repository.JzdSchoolRepository;
import com.ichzh.physicalFitness.service.JzdSchoolQueryRecordService;
import com.ichzh.physicalFitness.service.JzdSchoolService;
import com.ichzh.physicalFitness.service.RecruitRangeQueryRecordService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/jzdschool"})
public class JzdSchoolApi {
	
	@Autowired
    JzdSchoolRepository jzdSchoolRepository;
	@Autowired
	JzdSchoolQueryRecordService jzdSchoolQueryRecordService;
	@Autowired
	UserService userService;
	@Autowired
	RecruitRangeQueryRecordService recruitRangeQueryRecordService;
	@Autowired
	JzdSchoolService jzdSchoolService;
	
	/**
	 * 查询居住地对应学校
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param streetName      居住地_街道名称
	 * @param communityName   居住地_社区名称
	 * @param detailAddress   居住地_住址名称
	 * @return
	 */
	@RequestMapping(value="/queryJzdSchool", method= {RequestMethod.POST})
	public OperaResult queryJzdSchool(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "streetName", required = false) String streetName,
			@RequestParam(value = "communityName", required = false) String communityName,
			@RequestParam(value = "detailAddress", required = false) String detailAddress,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<JzdSchool> JzdSchools = jzdSchoolRepository.queryBy(serviceBlock, town, streetName, communityName, detailAddress);
			//查询居住地对应学校用的居住地
			String jzdAddress = detailAddress;
			if (StringUtils.isEmpty(detailAddress)) {
				if (StringUtils.isNotEmpty(communityName)) {
					jzdAddress = communityName;
				}else if(StringUtils.isNotEmpty(streetName)){
					jzdAddress = streetName;
				}
			}
			
			if (StringUtils.isNotEmpty(jzdAddress) && JzdSchools != null && JzdSchools.size() > 0) {
				jzdSchoolQueryRecordService.executeAterJzdSchoolQuerySuccess(memberId, jzdAddress, serviceBlock, town);
			}
			
			//补充其它信息（学校详情信息、收藏信息）
			jzdSchoolService.setOtherData(JzdSchools, memberId);
			
			result.setResultCode(OperaResult.Success);
			result.setData(JzdSchools);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 模糊搜索街道信息
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param streetName      居住地_街道名称
	 * @return
	 */
	@RequestMapping(value="/queryStreetName", method= {RequestMethod.POST})
	public OperaResult queryStreetName(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "streetName", required = false) String streetName) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<StreetCommunityDto> admissionCones = jzdSchoolRepository.queryStreetName(serviceBlock, town, streetName);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 模糊搜索社区信息
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param streetName      居住地_街道名称
	 * @param communityName   居住地_社区名称
	 * @return
	 */
	@RequestMapping(value="/queryCommunityName", method= {RequestMethod.POST})
	public OperaResult queryCommunityName(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "streetName", required = false) String streetName,
			@RequestParam(value = "communityName", required = false) String communityName) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<StreetCommunityDto> admissionCones = jzdSchoolRepository.queryCommunityName(serviceBlock, town, streetName, communityName);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 模糊搜索地址信息
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param streetName      居住地_街道名称
	 * @param communityName   居住地_社区名称
	 * @param detailAddress   居住地_住址名称
	 * @return
	 */
	@RequestMapping(value="/queryDetailAddress", method= {RequestMethod.POST})
	public OperaResult queryDetailAddress(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "streetName", required = false) String streetName,
			@RequestParam(value = "communityName", required = false) String communityName,
			@RequestParam(value = "detailAddress", required = false) String detailAddress) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<StreetCommunityDto> admissionCones = jzdSchoolRepository.queryDetailAddress(serviceBlock, town, streetName, communityName, detailAddress);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 模糊搜索查询学校名称
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param schoolName    查询学校名称
	 * @return
	 */
	@RequestMapping(value="/queryJzdSchoolInfo", method= {RequestMethod.POST})
	public OperaResult queryJzdSchoolInfo(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "querySchoolName", required = false) String querySchoolName) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<SchoolDto> admissionCones = jzdSchoolRepository.queryJzdSchoolInfo(serviceBlock, town,querySchoolName);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 查询街道社区地址列表
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param schoolName    查询学校名称
	 * @return
	 */
	@RequestMapping(value="/queryStreetCommunityAddress", method= {RequestMethod.POST})
	public OperaResult queryStreetCommunityAddress(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "querySchoolName", required = false) String querySchoolName,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		Map<String, Object> retData = new HashMap<String, Object>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<RangeDto> admissionCones = jzdSchoolRepository.queryStreetCommunityAddress(serviceBlock, town,querySchoolName);
			jzdSchoolService.setAddress(admissionCones);
			result.setResultCode(OperaResult.Success);
			if (admissionCones != null && admissionCones.size() > 2) {
				retData.put("data", admissionCones.subList(0, 2)) ;
				retData.put("ifHasMore", "1");
			}else {
				retData.put("data", admissionCones) ;
				retData.put("ifHasMore", "0");
			}
			result.setData(retData);
			if (admissionCones != null && admissionCones.size() > 0) {
				recruitRangeQueryRecordService.executeAterRecruitRangeQuerySuccess(memberId, "", querySchoolName, serviceBlock, town);
			}
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	
	/**
	 * 查询街道社区地址列表——更多
	 * @param serviceBlock  居住地所属服务模块
	 * @param town          居住地所属区  
	 * @param schoolName    查询学校名称
	 * @return
	 */
	@RequestMapping(value="/queryStreetCommunityAddress4More", method= {RequestMethod.POST})
	public OperaResult queryStreetCommunityAddress4More(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "querySchoolName", required = false) String querySchoolName,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		Map<String, Object> retData = new HashMap<String, Object>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			retData = jzdSchoolService.queryStreetCommunityAddress4More(serviceBlock, town, querySchoolName, memberId);
			
			result.setResultCode(OperaResult.Success);
			result.setData(retData);
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
}
