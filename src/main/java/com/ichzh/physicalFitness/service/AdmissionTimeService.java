package com.ichzh.physicalFitness.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.AdmissionTime;

public interface AdmissionTimeService {

	/**
	 * 将给定的多个入学时间按时间分组项进行分组
	 * @param addmisssionTimes
	 * @return Map<String, List<AdmissionTime>>  key 分组名 value 分组下对应的入学时间
	 */
	public Map<String, List<AdmissionTime>> groupAdmissionTime(List<AdmissionTime> addmisssionTimes);
}
