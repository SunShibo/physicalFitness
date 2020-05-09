package com.ichzh.physicalFitness.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.AddressInfo;
import com.ichzh.physicalFitness.repository.AddressInfoRepository;
import com.ichzh.physicalFitness.service.AddressInfoService;

@Service("addressInfoService")
public class AddressInfoServiceImpl implements AddressInfoService {

	@Autowired
	AddressInfoRepository addressInfoRepository;
	
	/**
	 * 保存默认地址信息
	 * @param memberId          会员标识号.
	 * @param addressDetail     地址详情.
	 * @param longitude         经度
	 * @param dimension         纬度
	 * @param addressType       地址类型.
	 * @return
	 */
	public boolean writeDefaultAddressInfo(String memberId, String addressDetail, BigDecimal longitude,
			BigDecimal dimension, Integer addressType) {
		
		AddressInfo addressInfo = new AddressInfo();
		// 检查是否已经设置过默认地址
		List<AddressInfo> addressInfoes = addressInfoRepository.findByMemberIdAndAddressTypeAndIfDefault(memberId, addressType, 1);
		if (addressInfoes != null && addressInfoes.size() >0) {
			addressInfo = addressInfoes.get(0);
		}
		addressInfo.setAddressDetail(addressDetail);
		addressInfo.setLongitude(longitude);
		addressInfo.setDimension(dimension);
		addressInfo.setIfDefault(1);
		addressInfo.setMemberId(memberId);
		addressInfo.setAddressType(addressType);
		
		addressInfoRepository.save(addressInfo);
		
		return true;
	}

	
	
}
