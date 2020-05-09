package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "condition_residence_permit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConditionResidencePermit {

	   @Id
	    @GeneratedValue
	    @Column(name="permit_id")
	    private Integer permitId;
	    
	    @Column(name="service_block")
	    private Integer serviceBlock;

	    // 所属区
	    @Column(name="town")
	    private Integer town;
	    
	    // 户籍
	    @Column(name="household_registration")
	    private Integer householdRegistration;
	    
	    // 居住
	    @Column(name="residence")
	    private Integer residence;
	    
	    //务工
	    @Column(name="labor")
	    private Integer labor;
	    
	    // 居住证
	    @Column(name="residence_permit")
	    private Integer residencePermit;
	    
	    //年度
	    @Column(name="yearYear")
	    private Integer year_year;
	    
	    //是否可入学
	    @Column(name="is_can_admission")
	    private Integer isCanAdmission;
}
