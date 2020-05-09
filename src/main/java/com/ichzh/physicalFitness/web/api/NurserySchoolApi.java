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
import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.repository.NurserySchoolRepository;
import com.ichzh.physicalFitness.service.NurserySchoolService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/nurseryschool"})
public class NurserySchoolApi {

	@Autowired
    NurserySchoolRepository nurserySchoolRepository;
	@Autowired
    NurserySchoolService nurserySchoolService;
	@Autowired
    UserService userService;
	
	/**
	 * 查询上幼儿园入学优选定位附近的园根据经纬度查询附近5公里范围内的幼儿园
	 * @param serviceBlock 服务模块  
	 * @param town   所属区
	 * @param schoolType   学校类型 1：表示公办，2：表示民办，3：表示示范，4：表示民办普惠
	 * @return
	 */
	@RequestMapping(value="/queryNurserySchoolMapRange", method= {RequestMethod.POST})
	public OperaResult queryNurserySchoolMapRange(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "maxDistance", required = false) Integer maxDistance,
			@RequestParam(value = "schoolType", required = false) Integer schoolType) {
		OperaResult result = new OperaResult();
		try
		{
			if(serviceBlock != Constant.DICT_ID_10001) {
				result.setResultDesc("服务模块应为幼儿园");
				result.setResultCode(OperaResult.Error);
				return result;
			}
			List<Map> studentStatuses = nurserySchoolRepository.queryNurserySchoolMapRange(serviceBlock, null,latitude,longitude,maxDistance,schoolType);
			
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	
	@RequestMapping(value="/queryNearNurserySchoolMapRange", method= {RequestMethod.POST})
	public OperaResult queryNearNurserySchoolMapRange(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "maxDistance", required = false) Integer maxDistance,
			@RequestParam(value = "schoolType", required = false) Integer schoolType) {
		OperaResult result = new OperaResult();
		try{
			if(serviceBlock != Constant.DICT_ID_10001) {
				result.setResultDesc("服务模块应为幼儿园");
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			List<Map> studentStatuses = nurserySchoolRepository.queryNurserySchoolMapRange(serviceBlock, null,
					latitude,longitude,maxDistance,schoolType);
			
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	
	/**
	 * 查询上幼儿园入学优选分类查询幼儿园列表
	 * @param serviceBlock 服务模块  
	 * @param town   所属区
	 * @param organizers 举办者
	 * @param isModel 是否示范
	 * @param isInclusive 是否民办普惠
	 * @return
	 */
	@RequestMapping(value="/queryNurserySchool", method= {RequestMethod.POST})
	public OperaResult queryNurserySchool(HttpServletRequest request,
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "organizers", required = false) Integer organizers,
			@RequestParam(value = "isModel", required = false) Integer isModel,
			@RequestParam(value = "isInclusive", required = false) Integer isInclusive) {
		OperaResult result = new OperaResult(); 
		try{
			Member member = userService.getCurrentLoginUser(request);
			List<NurserySchool> studentStatuses = nurserySchoolRepository.queryNurserySchool(serviceBlock, town,
					organizers,isModel,isInclusive, member.getMemberId());
			
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 查询上幼儿园入学优选分类查询筛选条件页查询举办者
	 * @param serviceBlock 服务模块  
	 * @param town   所属区
	 * @return
	 */
	@RequestMapping(value="/queryOrganizers", method= {RequestMethod.POST})
	public OperaResult queryOrganizers(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		OperaResult result = new OperaResult();
		try
		{
			List<StudentStatusDto> studentStatuses = nurserySchoolRepository.queryOrganizers(serviceBlock, town);
			
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 查询上幼儿园详情
	 * @param nurserySchoolId 幼儿园标识号 
	 * @return
	 */
	@RequestMapping(value="/getNurserySchoolById", method= {RequestMethod.POST})
	public OperaResult getNurserySchoolById(
			@RequestParam(value = "nurserySchoolId", required = true) Integer nurserySchoolId,HttpServletRequest request) {
		OperaResult result = new OperaResult();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				return result;
			}
			
			NurserySchool school = nurserySchoolRepository.findOne(nurserySchoolId);
			//将学校简介的五类信息拆分为多个段落
			nurserySchoolService.splitSchoolProfile(school);
			//设置学校标签
			nurserySchoolService.setSchoolLabel(school);
			//设置收藏状态
			nurserySchoolService.setColStatus(memberId, school);
			//设置举办者描述
			nurserySchoolService.setSchoolRunningTypeData(school);
			
			result.setResultCode(OperaResult.Success);
			result.setData(school);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 根据幼儿园名称查询幼儿园信息
	 * @param schoolName  学校名称
	 * @param town 学校所属区   该参数为了缴费拦截用
	 * @param serviceBlock  服务模块   该参数为了缴费拦截用
	 * @return
	 */
	@RequestMapping(value="/queryNurserySchoolBySchoolName", method= {RequestMethod.POST})
	public OperaResult queryNurserySchoolBySchoolName(
			@RequestParam(value = "schoolName", required = true) String schoolName,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock
			) {
		OperaResult result = new OperaResult();
		try 
		{
			List<Object> schoolChoices = nurserySchoolRepository.queryNurserySchoolBy(schoolName, town);
			result.setResultCode(OperaResult.Success);
			result.setData(schoolChoices);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
}
