package com.ichzh.physicalFitness.dto;

import lombok.Data;

@Data
public class SchoolExportLevelDto {

	// 学校等级名称
	private String levelName;
	// 升学占比
	private Float sxRatio;
	// 当前等级下所有学校在学校优选表中的主键集合格式：choiceId##choiceId
	private String schoolChoiceIds;
}
