package com.ichzh.physicalFitness.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Breadcrumb implements Serializable{

	private String menuName1;
	private String menuUrl1;
	private String menuName2;
	private String menuUrl2;
	private String title;
}
