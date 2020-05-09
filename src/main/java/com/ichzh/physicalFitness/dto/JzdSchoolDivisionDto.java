package com.ichzh.physicalFitness.dto;

import lombok.Data;

/**
 * 居住地对应的行政区划
 * @author yjf
 *
 */
@Data
public class JzdSchoolDivisionDto {

	//街道个数 
	private int streetNum;
	//社区个数
	private int communityNum;
	//地址个数
	private int detailAddressNum;
	
	
}
