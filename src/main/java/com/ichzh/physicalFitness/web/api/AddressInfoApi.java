package com.ichzh.physicalFitness.web.api;

import java.math.BigDecimal;
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
import com.ichzh.physicalFitness.model.AddressInfo;
import com.ichzh.physicalFitness.repository.AddressInfoRepository;
import com.ichzh.physicalFitness.service.AddressInfoService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/addressinfo"})
public class AddressInfoApi {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressInfoService addressInfoService;
	
	@Autowired
	private AddressInfoRepository addressInfoRepository;
	
	//地址类型：家庭地址
	private  int ADDRESS_TYPE_FAMILY = 1;
	
	//地址类型：单位地址
    private  int ADDRESS_TYPE_DEPARTMENT = 2;

	/**
	 * 查询默认的家庭地址
	 */
	@RequestMapping(value="/queryFamilyAddress4default", method= {RequestMethod.POST})
	public OperaResult queryFamilyAddress4default(HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
			}else {
			
				List<AddressInfo> addressInfoes =  addressInfoRepository.findByMemberIdAndAddressTypeAndIfDefault(memberId, 
						this.ADDRESS_TYPE_FAMILY, 1);
				if (addressInfoes != null) {
					result.setResultCode(OperaResult.Success);
					result.setData(addressInfoes.get(0));
				}else {
					result.setResultCode(OperaResult.Success);
				}
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 查询默认的单位地址
	 */
	@RequestMapping(value="/queryDepartmentAddress4default", method= {RequestMethod.POST})
	public OperaResult queryDepartmentAddress4default(HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
			}else {
			
				List<AddressInfo> addressInfoes =  addressInfoRepository.findByMemberIdAndAddressTypeAndIfDefault(memberId, 
						this.ADDRESS_TYPE_DEPARTMENT, 1);
				if (addressInfoes != null) {
					result.setResultCode(OperaResult.Success);
					result.setData(addressInfoes.get(0));
				}else {
					result.setResultCode(OperaResult.Success);
				}
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 写入默认的家庭地址
	 * @param addressDetail  家庭地址
	 * @param longitude  经度
	 * @param dimension  维度
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/writeDefaultFamilyAddress", method= {RequestMethod.POST})
	public OperaResult writeDefaultFamilyAddress(@RequestParam(value = "addressDetail", required = true) String addressDetail,
			@RequestParam(value = "longitude", required = true) BigDecimal longitude, 
			@RequestParam(value = "dimension", required = true) BigDecimal dimension, 
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
				boolean ret =  addressInfoService.writeDefaultAddressInfo(memberId, addressDetail, longitude, dimension, this.ADDRESS_TYPE_FAMILY);
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
	 * 写入默认的单位地址
	 * @param addressDetail  单位地址
	 * @param longitude  经度
	 * @param dimension  维度
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/writeDefaultDepartmentAddress", method= {RequestMethod.POST})
	public OperaResult writeDefaultDepartmentAddress(@RequestParam(value = "addressDetail", required = true) String addressDetail,
			@RequestParam(value = "longitude", required = true) BigDecimal longitude, 
			@RequestParam(value = "dimension", required = true) BigDecimal dimension, 
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
				boolean ret =  addressInfoService.writeDefaultAddressInfo(memberId, addressDetail, longitude, dimension, this.ADDRESS_TYPE_DEPARTMENT);
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
}
