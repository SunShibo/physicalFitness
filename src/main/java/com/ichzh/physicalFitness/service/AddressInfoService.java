package com.ichzh.physicalFitness.service;

import java.math.BigDecimal;

public interface AddressInfoService {

	/**
	 * 保存默认地址信息
	 * @param memberId          会员标识号.
	 * @param addressDetail     地址详情.
	 * @param longitude         经度
	 * @param dimension         纬度
	 * @param addressType       地址类型.
	 * @return
	 */
	public boolean writeDefaultAddressInfo(String memberId, String addressDetail, 
			BigDecimal longitude, BigDecimal dimension, Integer addressType);
}
