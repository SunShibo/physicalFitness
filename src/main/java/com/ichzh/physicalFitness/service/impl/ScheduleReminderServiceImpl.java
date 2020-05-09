package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.SubscribeMessage;
import com.ichzh.physicalFitness.model.EnrollmentSchedule;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.ScheduleReminder;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.EnrollmentScheduleRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.ScheduleReminderRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.ScheduleReminderService;
import com.ichzh.physicalFitness.service.SendTemplateMessageService;
import com.ichzh.physicalFitness.service.WeChatService;
import com.ichzh.physicalFitness.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("scheduleReminderService")
public class ScheduleReminderServiceImpl implements ScheduleReminderService {

	@Autowired
	ScheduleReminderRepository scheduleReminderRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Resource(name = "selfConfig")
	private SelfConfig selfConfig;
	
	@Resource(name = "weChatService")
    private WeChatService weChatService;
	@Autowired
	private EnrollmentScheduleRepository enrollmentScheduleRepository;
	@Autowired
	private SendTemplateMessageService sendTemplateMessageService;
	
	private Map<String, String> memberCache = new HashMap<>();
	
	private Map<String, String> unifiedAccountCache = new HashMap<>();
	
	
	/**
	 * 保存入学日程提醒设置信息.
	 * @param memberId       会员标识号.
	 * @param serviceBlock   入学阶段.
	 * @param town           区.
	 * @param admissionMode  入学方式.
	 * @return
	 */
	public boolean saveScheduleReminder(String memberId, Integer serviceBlock, Integer town, Integer admissionMode) {
		
		//收藏的提醒是否已经达到了3个，如果已经到达3个则不能新增
		List<ScheduleReminder> ScheduleReminders = scheduleReminderRepository.findByMemberId(memberId);
		if (ScheduleReminders != null  && ScheduleReminders.size() >= 3) {
			return false;
		}
		ScheduleReminder scheduleReminder =  new ScheduleReminder();
		scheduleReminder.setMemberId(memberId);
		scheduleReminder.setServiceBlock(serviceBlock);
		scheduleReminder.setTown(town);
		scheduleReminder.setAdmissionMode(admissionMode);
		scheduleReminderRepository.saveAndFlush(scheduleReminder);
		
		return true;
	}

	public void setOtherData(List<ScheduleReminder> scheduleReminders) {
		if (scheduleReminders != null && scheduleReminders.size() > 0) {
			for (ScheduleReminder oneScheduleReminder : scheduleReminders) {
				oneScheduleReminder.setServiceBlockName(cacheApplicationService.getDictName(oneScheduleReminder.getServiceBlock()));
				oneScheduleReminder.setTownName(cacheApplicationService.getTownNameByTown(oneScheduleReminder.getTown()));
				oneScheduleReminder.setAdmissionModeName(cacheApplicationService.getDictName(oneScheduleReminder.getAdmissionMode()));
			}
		}
		
	}
	
	
	public void scheduleReminderPush() {
		log.info("---开始推送入学日程---");
		// 缓存数据
		cacheData();
		// 查询需要推送的数据
		List<ScheduleReminder> lstScheduleReminder = scheduleReminderRepository.queryNeedPushScheduleReminder();
		
		if(!CollectionUtils.isEmpty(lstScheduleReminder)) {
			for(ScheduleReminder oneSchedule : lstScheduleReminder) {
				oneSchedule.setOpenId(memberCache.get("reminder_" + oneSchedule.getMemberId()));
				//入学阶段
				Integer serviceBlock = oneSchedule.getServiceBlock();
				//入学区
				Integer town = oneSchedule.getTown();
				//入学方式
				Integer admissionMode = oneSchedule.getAdmissionMode();
				//家长做什么
				String parentDoSomething = oneSchedule.getParentDoSomething();
				Date scheduleDate = oneSchedule.getScheduleDate();
				
				// 判断是否关注过公众号
				if(unifiedAccountCache.containsKey(oneSchedule.getOpenId())) {
					String gzhOpenId = unifiedAccountCache.get(oneSchedule.getOpenId());
					//关注过
					if(StringUtils.isNotEmpty(gzhOpenId)) {
						//记入模板消息日志的发送内容
						String sendContent = gzhOpenId+"_"+town.toString()+"_"+serviceBlock.toString()+"_"+admissionMode.toString()+"_"+CommonUtil.DateToDateString(scheduleDate);
						
						String first = "入学日历已更新";
						String keyword1 = cacheApplicationService.getTownNameByTown(town)+" "+
									cacheApplicationService.getDictName(serviceBlock) + " " + cacheApplicationService.getDictName(admissionMode);
						String keyword2 = CommonUtil.getChineseDateString(scheduleDate) + " " + parentDoSomething;
					
						sendTemplateMessageService.sendEnrollmentScheduleMsg(gzhOpenId, first, keyword1, keyword2, sendContent);
					}
				}
			}
		}
		log.info("---推送入学日程结束---");
	}
	
	
	public void pushSubscribeMessageByOpenId(String weappKey, ScheduleReminder scheduleReminder) {
		// 获取消息推送数据对象模板
		SubscribeMessage subscribeMessage = selfConfig.getSubscribeMessageMap().get(weappKey);
		
		Map<String, Map<String, String>> dataMap = new HashMap<>();
		
		Map<String, String> thing1Map = new HashMap<>();
		thing1Map.put("value", "好入学入学政策");
		dataMap.put("thing1", thing1Map);
		
		Map<String, String> time2Map = new HashMap<>();
		time2Map.put("value", CommonUtil.DateToDateString(scheduleReminder.getScheduleDate()));
		dataMap.put("time2", time2Map);
		
		Map<String, String> thing3Map = new HashMap<>();
		thing3Map.put("value", scheduleReminder.getParentDoSomething());
		dataMap.put("thing3", thing3Map);
		
		Map<String, String> date4Map = new HashMap<>();
		date4Map.put("value", CommonUtil.DateTimeToString(new Date()));
		dataMap.put("date4", date4Map);
		
		//subscribeMessage.setPage("pages/remind/list");
		subscribeMessage.setData(dataMap);
		subscribeMessage.setTouser(scheduleReminder.getOpenId());
		
		try {
			Map<String, String> stringStringMap = weChatService.sendSubscribeMessage(subscribeMessage);
	        System.out.println(stringStringMap);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关注公众号之后，向公众号中推送一条消息.
	 * @param gzhOpenId  公众号的openId
	 */
	public void pushGzhMsgAfterFollow(String gzhOpenId) {
		
		//查询明天是否有入学日程提醒
		Date tomorrow = CommonUtil.addDate(new Date(), 1);
		// sendTemplateMessageService
		List<EnrollmentSchedule> enrollmentSchedules = enrollmentScheduleRepository.queryEnrollmentScheduleByOpenId(gzhOpenId, CommonUtil.DateToDateString(tomorrow));
		if (enrollmentSchedules != null && enrollmentSchedules.size() > 0) {
			for (EnrollmentSchedule oneSchedule : enrollmentSchedules) {
				//入学阶段
				Integer serviceBlock = oneSchedule.getServiceBlock();
				//入学区
				Integer town = oneSchedule.getTown();
				//入学方式
				Integer admissionMode = oneSchedule.getAdmissionMode();
				//家长做什么
				String parentDoSomething = oneSchedule.getParentDoSomething();
				Date scheduleDate = oneSchedule.getScheduleDate();
				
				//关注后的下一天有日程提醒信息
				if (StringUtils.isNotEmpty(parentDoSomething)) {
					//记入模板消息日志的发送内容
					String sendContent = gzhOpenId+"_"+town.toString()+"_"+serviceBlock.toString()+"_"+admissionMode.toString()+"_"+CommonUtil.DateToDateString(tomorrow);
					
					String first = "入学日历已更新";
					String keyword1 = cacheApplicationService.getTownNameByTown(town)+" "+
								cacheApplicationService.getDictName(serviceBlock) + " " + cacheApplicationService.getDictName(admissionMode);
					String keyword2 = CommonUtil.getChineseDateString(scheduleDate) + " " + parentDoSomething;
				
					sendTemplateMessageService.sendEnrollmentScheduleMsg(gzhOpenId, first, keyword1, keyword2, sendContent);
				}else{
					//记入模板消息日志的发送内容
					String sendContent = gzhOpenId+"_"+town.toString()+"_"+serviceBlock.toString()+"_"+admissionMode.toString();
					String first = "入学日历已订阅";
					String keyword1 = cacheApplicationService.getTownNameByTown(town)+" "+
							cacheApplicationService.getDictName(serviceBlock) + " " + cacheApplicationService.getDictName(admissionMode);
					String keyword2 = "成功订阅";
					
					sendTemplateMessageService.sendEnrollmentScheduleMsg(gzhOpenId, first, keyword1, keyword2, sendContent);
				}
			}
		}
		
	}

	private void cacheData() {
		List<Member> lstMember = memberRepository.findAll();
		if(!CollectionUtils.isEmpty(lstMember)) {
			for(Member member : lstMember) {
				memberCache.put("reminder_" + member.getMemberId(), member.getMemberWeChat());
			}
		}
		
		List<UnifiedAccount> lstUnifiedAccount = scheduleReminderRepository.queryUnifiedAccount();
		if(!CollectionUtils.isEmpty(lstUnifiedAccount)) {
			for(UnifiedAccount unifiedAccount : lstUnifiedAccount) {
				unifiedAccountCache.put(unifiedAccount.getWechatOpenId(), unifiedAccount.getGzhOpenId());
			}
		}
	}
}
