package com.ichzh.physicalFitness.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 派位范围模块—学校信息
 * @author yjf
 *
 */
@Data
public class AidedAllocateRangePrimSchoolDto {

	/**
	 * 学校名称
	 */
	private String  schoolName;
	/**
	 * 学校代码
	 */
	private String schoolCode;
	
	/**
	 * 派位类型
	 */
	private Integer aidedAllocateKind;
	/**
	 * 派位类型名称
	 */
	private String aidedAllocateKindName;
	/**
	 * 学校ID
	 */
	private Integer schoolId;
	
	/**
	 * 学校是否被收藏( 1 收藏  0 未收藏)
	 */
	private Integer colStatus;
	
	// 经度
    private BigDecimal longitude;

    // 维度
    private BigDecimal dimension;
}
