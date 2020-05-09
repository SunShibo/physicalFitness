package com.ichzh.physicalFitness.dto;

import java.util.List;

import lombok.Data;

@Data
public class ExportDemoDto {

	/**
	 * 考试类别
	 */
	private String examKind;
	/**
	 * 考点校名称
	 */
	private String schoolName;
	/**
	 * 考试时间
	 */
	private String examTime;
	/**
	 * 应发金额-合计
	 */
	private String amountTotal;
	/**
	 * 代扣税金-合计
	 */
	private String taxTotal;
	/**
	 * 实发金额-合计
	 */
	private String amoutFinalTotal;
	/**
	 * 实发金额-合计-大写
	 */
	private String amoutFinalTotalCN;
	
	/**
	 * 费用发放明细
	 */
	private  List<ExportDemoDto2>  costIssuanceDetail;
	
	 
}
