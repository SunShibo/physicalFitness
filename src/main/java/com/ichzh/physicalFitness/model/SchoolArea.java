package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "school_area")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolArea {

	   @Id
	    @GeneratedValue
	    @Column(name="area_id")
	    private Integer areaId;
	    
	    //服务模块 
	    @Column(name="service_block")
	    private Integer serviceBlock;
	    
	    //区
	    @Column(name="town")
	    private Integer town;
	    
	    //学校代码
	    @Column(name="school_code")
	    private String schoolCode;
	    
	    //学校名称
	    @Column(name="school_name")
	    private String schoolName;
	    
	    //校区名称
	    @Column(name="campus_name")
	    private String campusName;
	    
	    //片区名称
	    @Column(name="area_name")
	    private String areaName;
}
