package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.ScheduleReminder;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.ext.IScheduleReminderRepositoryExt;

@Repository
public interface ScheduleReminderRepository extends BaseRepository<ScheduleReminder, Integer>, IScheduleReminderRepositoryExt{

	public List<ScheduleReminder> findByMemberIdAndServiceBlockAndTownAndAdmissionMode(String memberId, 
			Integer serviceBlock, Integer town, Integer admissionMode);
	
	public List<ScheduleReminder> findByMemberId(String memberId);

}
