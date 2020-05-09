package com.ichzh.physicalFitness.web.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.model.SchoolCollection;
import com.ichzh.physicalFitness.repository.SchoolCollectionRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.SchoolCollectionService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/schoolcollection"})
public class SchoolCollectionApi {

	@Autowired
	UserService userService;
	@Autowired
	SchoolCollectionService schoolCollectionService;
	@Autowired
	SchoolCollectionRepository schoolCollectionRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	/**
	 * 收藏或取消学校
	 * @param serviceBlock    服务模块         
	 * @param schoolId        收藏学校的标识号  
	 * @param collectionOrCancel   1: 收藏 0:取消
	 * @return
	 */
	@RequestMapping(value="/collectionOrCancel", method= {RequestMethod.POST})
	public OperaResult collectionOrCancel(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "schoolId", required = true) Integer schoolId, 
			@RequestParam(value = "collectionOrCancel", required = true) Integer collectionOrCancel, 
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Success);
				//1: 操作失败，当前用户的登录状态已失效
				resultData.put("write_result", "0");
				result.setData(resultData);
			}else {
				boolean ret = schoolCollectionService.collectionOrCancel(memberId, serviceBlock, schoolId, collectionOrCancel);
				if (ret) {
					result.setResultCode(OperaResult.Success);
					// 操作成功
					resultData.put("write_result", "1");
					result.setData(resultData);
				}else {
					result.setResultCode(OperaResult.Error);
				}
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	
	/**
	 * 查询已收藏的学校
	 * @param serviceBlock    服务模块         
	 * @param schoolId        收藏学校的标识号  
	 * @return
	 */
	@RequestMapping(value="/querySchoolOfCollected", method= {RequestMethod.POST})
	public OperaResult querySchoolOfCollected(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
			}else {
			
				List<SchoolCollection> schoolCollectiones = cacheApplicationService.querySchoolCollectionBy(memberId);
				//设置学校名称
				schoolCollectionService.setSchoolName(schoolCollectiones);
				
				result.setResultCode(OperaResult.Success);
				result.setData(schoolCollectiones);
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
