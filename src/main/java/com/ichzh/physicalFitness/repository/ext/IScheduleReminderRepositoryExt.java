package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.ScheduleReminder;
import com.ichzh.physicalFitness.model.UnifiedAccount;

public interface IScheduleReminderRepositoryExt {

	public boolean delScheduleReminderBy(String memberId, Integer serviceBlock, Integer town, Integer admissionMode);
	public List<ScheduleReminder> queryNeedPushScheduleReminder();
	
	public List<UnifiedAccount> queryUnifiedAccount();
}
