package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.AdmissionTime;
import com.ichzh.physicalFitness.service.AdmissionTimeService;

@Service("admissionTimeService")
public class AdmissionTimeServiceImpl implements AdmissionTimeService {

	/**
	 * 将给定的多个入学时间按时间分组项进行分组
	 * @param addmisssionTimes
	 * @return Map<String, List<AdmissionTime>>  key 分组名 value 分组下对应的入学时间
	 */
	public Map<String, List<AdmissionTime>> groupAdmissionTime(List<AdmissionTime> addmisssionTimes) {
		
		Map<String, List<AdmissionTime>> ret = new LinkedHashMap<String, List<AdmissionTime>>();
		if (addmisssionTimes != null && addmisssionTimes.size() > 0) {
			for (AdmissionTime oneAdmissionTime : addmisssionTimes) {
				String timeGroup = oneAdmissionTime.getTimeGroup();
				
				List<AdmissionTime> currentAdmTimes = ret.get(timeGroup);
				if (currentAdmTimes == null) {
					currentAdmTimes = new ArrayList<AdmissionTime>();
				}
				currentAdmTimes.add(oneAdmissionTime);
				ret.put(timeGroup, currentAdmTimes);
			}
		}
		return ret;
	}

	
}
