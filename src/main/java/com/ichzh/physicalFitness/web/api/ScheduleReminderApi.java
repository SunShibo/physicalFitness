package com.ichzh.physicalFitness.web.api;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.ScheduleReminder;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.ScheduleReminderRepository;
import com.ichzh.physicalFitness.repository.UnifiedAccountRepository;
import com.ichzh.physicalFitness.service.ScheduleReminderService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/scheduleReminder"})
public class ScheduleReminderApi {
	
	@Autowired
	UserService userService;
	@Autowired
	ScheduleReminderRepository scheduleReminderRepository;
	@Autowired
	ScheduleReminderService scheduleReminderService;
	@Autowired
	private UnifiedAccountRepository unifiedAccountRepository;

	/**
	 * 保存入学日程提醒
	 * @param serviceBlock
	 * @param town
	 * @param admissionMode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addScheduleReminder", method= {RequestMethod.POST})
	public OperaResult addScheduleReminder(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "admissionMode", required = true) Integer admissionMode, 
			HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				//0: 操作失败，当前用户的登录状态已失效
				resultData.put("write_result", "0");
				result.setData(resultData);
				return result;
			}else {
				List<ScheduleReminder> scheduleReminders = scheduleReminderRepository.
						findByMemberIdAndServiceBlockAndTownAndAdmissionMode(memberId, serviceBlock, town, admissionMode);
				if (scheduleReminders != null && scheduleReminders.size() > 0) {
					result.setResultCode(OperaResult.Error);
					//1: 操作失败，已经存在
					resultData.put("write_result", "1");
					result.setData(resultData);
					return result;
				}
				
				boolean ret = scheduleReminderService.saveScheduleReminder(memberId, serviceBlock, town, admissionMode);
				if (ret) {
					result.setResultCode(OperaResult.Success);
					// 操作成功
					resultData.put("write_result", "2");
					// 返回是否关注公众号
					Member member = userService.getCurrentLoginUser(request);
					String isFollow = "0";
					UnifiedAccount unifiedAccount = unifiedAccountRepository.findByOpenIdAndType(member.getMemberWeChat(), 1);
					if(unifiedAccount != null) {
						UnifiedAccount unifiedAccountUnion = unifiedAccountRepository.findByUnionIdAndType(unifiedAccount.getUnionId(), 2);
						isFollow =  unifiedAccountUnion != null ? "1" : "0";
					}
					resultData.put("isFollow", isFollow);
					result.setData(resultData);
				}else {
					result.setResultCode(OperaResult.Error);
				}
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	@RequestMapping(value="/removeScheduleReminder", method= {RequestMethod.POST})
	public OperaResult removeScheduleReminder(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "admissionMode", required = true) Integer admissionMode, 
			HttpServletRequest request) {
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
				//0: 操作失败，当前用户的登录状态已失效
				resultData.put("write_result", "0");
				result.setData(resultData);
				return result;
			}else {
				boolean ret = scheduleReminderRepository.delScheduleReminderBy(memberId, serviceBlock, town, admissionMode);
				if (ret) {
					result.setResultCode(OperaResult.Success);
					// 1: 操作成功
					resultData.put("write_result", "1");
					result.setData(resultData);
				}else {
					result.setResultCode(OperaResult.Error);
				}
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	@RequestMapping(value="/queryScheduleReminder", method= {RequestMethod.POST})
	public OperaResult queryScheduleReminder(HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		try
		{
			String memberId = userService.getCurrentLoginUserId(request);
			if (StringUtils.isEmpty(memberId)) {
				result.setResultCode(OperaResult.Error);
			}else {
			
				List<ScheduleReminder> scheduleReminders = scheduleReminderRepository.findByMemberId(memberId);
				//设置字典的显示名称
				scheduleReminderService.setOtherData(scheduleReminders);
				
				result.setResultCode(OperaResult.Success);
				result.setData(scheduleReminders);
			}
			
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
