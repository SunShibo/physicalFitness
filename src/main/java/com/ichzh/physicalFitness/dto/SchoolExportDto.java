package com.ichzh.physicalFitness.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SchoolExportDto {

	// 升学占比
	private Float sxRatio;
	
	//学校代码
	private String schoolCode;
	
	//学校名称
	private String schoolName;
	
	//校区名称
	private String campusName;
	
	// 学校在学校优选表中的主键.
	private Integer schoolId;
	
	// 经度
    private BigDecimal longitude;

    // 维度
    private BigDecimal dimension;
    
    //是否其它
    private Integer other;
    
    //当前学校的出口学校集合
    private List<SchoolExportDto> schoolExportDtoes;
    
    /**
	 * 学校是否被收藏( 1 收藏  0 未收藏)
	 */
	private Integer colStatus;
	
	 
}
