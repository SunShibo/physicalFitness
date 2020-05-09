package com.ichzh.physicalFitness.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdmissionConditonDto {

	//是否具备入学资格：0 不具备入学资格  1  具备入学资格
	private Integer isCanAdmission;
	
	//根据当前条件得出的结论为 不具备入学资格时，调整哪一个条件即可具备入学资格
	// 改变一个条件  
	private List<String> changeConditionKind;
	
	private List<StudentStatusDto> filterCones;
}
