package com.ichzh.physicalFitness.dto;

import lombok.Data;

@Data
public class RangeDto {
	//街道名称
	private String streetName;
	//社区名称
	private String communityName;
	//住址名称
	private String detailAddress;
	
	//将街道、社区和住址合并后的地址
	private String address;
}
