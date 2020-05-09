package com.ichzh.physicalFitness.dto;

import lombok.Data;

@Data
public class ExportDemoDto2 {

	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 证件号
	 */
	private String cardNo;
	/**
	 * 应发金额
	 */
	private String amount;
	/**
	 * 代扣税金
	 */
	private String tax;
	/**
	 * 实发金额
	 */
	private String amoutFinal;
}
