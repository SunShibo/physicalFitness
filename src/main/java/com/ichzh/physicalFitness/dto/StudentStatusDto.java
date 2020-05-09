package com.ichzh.physicalFitness.dto;

import lombok.Data;

/**
 * 学籍、户籍、居住地标签
 * @author yjf
 *
 */
@Data
public class StudentStatusDto {

	//学籍、户籍、居住地标签字典名称
	private String dictName;
	//学籍、户籍、居住地标签字典ID
	private Integer dictId;
}
