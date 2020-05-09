package com.ichzh.physicalFitness.web.api;

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
import com.ichzh.physicalFitness.dto.AidedAllocateRangePrimSchoolDto;
import com.ichzh.physicalFitness.dto.SchoolDto;
import com.ichzh.physicalFitness.dto.StreetCommunityDto;
import com.ichzh.physicalFitness.model.AidedAllocateRange;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.AidedAllocateRangeRepository;
import com.ichzh.physicalFitness.service.AidedAllocateRangeService;
import com.ichzh.physicalFitness.service.AllocateRangeQueryRecordService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/aidedallocaterange"})
public class AidedAllocateRangeApi {

	@Autowired
	private AidedAllocateRangeRepository aidedAllocateRangeRepository;
	@Autowired
	private AidedAllocateRangeService aidedAllocateRangeService;
	@Autowired
	private UserService userService;
	@Autowired
	private AllocateRangeQueryRecordService allocateRangeQueryRecordService;
	@Autowired
	private ICacheApplicationService cacheApplicationService;
	/**
	 * 查询派位范围
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param schoolDistrictName   学区或片区名称
	 * @param schoolCode           小学\中学学校代码
	 * @param schoolName           小学\中学名称代码
	 * @return
	 */
	@RequestMapping(value="/queryAidedAllocateRange", method= {RequestMethod.POST})
	public OperaResult queryAidedAllocateRange(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolDistrictName", required = false) String schoolDistrictName,
			@RequestParam(value = "schoolCode", required = false) String schoolCode,
			@RequestParam(value = "schoolName", required = false) String schoolName,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<AidedAllocateRange> admissionCones = aidedAllocateRangeRepository.queryBy(serviceBlock, town, schoolDistrictName, schoolCode, schoolName);
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 模糊搜索学区或片区名称
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param schoolDistrictName   学区或片区名称
	 * @return
	 */
	@RequestMapping(value="/querySchoolDistrict", method= {RequestMethod.POST})
	public OperaResult querySchoolDistrict(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolDistrictName", required = false) String schoolDistrictName) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<StreetCommunityDto> admissionCones = aidedAllocateRangeRepository.querySchoolDistrict(serviceBlock, town, schoolDistrictName);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 模糊搜索小学或中学名称
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param schoolDistrictName   学区或片区名称
	 * @param schoolCode           小学\中学学校代码
	 * @param schoolName           小学\中学名称代码
	 * @return
	 */
	@RequestMapping(value="/queryPaiWeiSchoolName", method= {RequestMethod.POST})
	public OperaResult querySchoolName(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolDistrictName", required = false) String schoolDistrictName,
			@RequestParam(value = "schoolCode", required = false) String schoolCode,
			@RequestParam(value = "schoolName", required = false) String schoolName) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<SchoolDto> admissionCones = aidedAllocateRangeRepository.querySchoolName(serviceBlock, town, schoolDistrictName, schoolCode, schoolName);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionCones);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 查询派位范围中的小学
	 * @param serviceBlock  派位服务模块.
	 * @param town          派位所属区.
	 * @param schoolName    小学名称关键字 这里采用like查询
	 * @return List<AidedAllocateRangePrimSchoolDto>.
	 */
	@RequestMapping(value="/queryAidedAllocateRangePrimSchool", method= {RequestMethod.POST})
	public OperaResult queryAidedAllocateRangePrimSchool(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "schoolName", required = false) String schoolName) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<AidedAllocateRangePrimSchoolDto> aidedAllocateRangePriSchooles = aidedAllocateRangeRepository.
					queryAidedAllocateRangePrimSchoolBy(serviceBlock, town, schoolName);
			result.setResultCode(OperaResult.Success);
			result.setData(aidedAllocateRangePriSchooles);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 查询派位范围中的中学
	 * @param serviceBlock  派位服务模块.
	 * @param town          派位所属区.
	 * @param priSchCode    小学学校代码
	 * @return List<AidedAllocateRangePrimSchoolDto>.
	 */
	@RequestMapping(value="/queryAidedAllocateRangeMiddSchool", method= {RequestMethod.POST})
	public OperaResult queryAidedAllocateRangeMiddSchool(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "priSchCode", required = true) String priSchCode,HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<AidedAllocateRangePrimSchoolDto> aidedAllocateRangeMiddSchooles = aidedAllocateRangeRepository.
					queryAidedAllocateRangeMiddSchoolBy(serviceBlock, town, priSchCode);
			
			{
				//此段代码，查询限次用
				//小学
				SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(Constant.DICT_ID_10002, priSchCode);
				String priSchoolName = "";
				if (schoolChoice != null) {
					priSchoolName = schoolChoice.getSchoolName();
				}
				if (aidedAllocateRangeMiddSchooles != null && aidedAllocateRangeMiddSchooles.size() > 0) {
					allocateRangeQueryRecordService.executeAterAllocateRangeQuerySuccess(memberId, priSchCode, priSchoolName, serviceBlock, town);
				}
			}
			
			//设置收藏信息和学校经纬度信息
			aidedAllocateRangeService.setOtherDataForMiddleSchool(memberId, aidedAllocateRangeMiddSchooles);
			
			//对派位中学按照派位类型分组
			Map<String, List<AidedAllocateRangePrimSchoolDto>> middSchoolAfterGroup = 
					aidedAllocateRangeService.groupRangeMiddSchool(aidedAllocateRangeMiddSchooles);
			
			
			
			result.setResultCode(OperaResult.Success);
			result.setData(middSchoolAfterGroup);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	

}
