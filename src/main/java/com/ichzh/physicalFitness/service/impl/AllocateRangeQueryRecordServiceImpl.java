package com.ichzh.physicalFitness.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AllocateRangeQueryLog;
import com.ichzh.physicalFitness.model.AllocateRangeQueryRecord;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.JzdSchoolQueryLog;
import com.ichzh.physicalFitness.model.JzdSchoolQueryRecord;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.MemberCommodityNum;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.AllocateRangeQueryLogRepository;
import com.ichzh.physicalFitness.repository.AllocateRangeQueryRecordRepository;
import com.ichzh.physicalFitness.repository.MemberCommodityNumRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.service.AllocateRangeQueryRecordService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.MemberCommodityNumService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("allocateRangeQueryRecordService")
public class AllocateRangeQueryRecordServiceImpl implements AllocateRangeQueryRecordService {
	
	@Autowired
	AllocateRangeQueryRecordRepository allocateRangeQueryRecordRepository;
	@Autowired
	MemberCommodityNumService  memberCommodityNumService;
	@Autowired
	MemberCommodityNumRepository memberCommodityNumRepository;
	@Autowired
	AllocateRangeQueryLogRepository allocateRangeQueryLogRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;

	/**
	 * 执行派位范围查询限次策略
	 * @param commodity
	 * @param member
	 * @return  resultCode: maxLimitNum 已经超过最大查询次数（不能买也不能用豆换） ,需要提示用户已达最大查询上限，明天再来
     *                      limitNum    未超过最大查询次数，但免费和用豆换的次数已用完，需要提示用户成为会员或使用学豆
     *                      notLimit    未超过最大查询次数，但免费和用豆换的次数未用完，让用户查询	 * 
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request) {
		
		OperaResult ret = new OperaResult();
		//本次查询的小学代码（只有中学有派位范围）
		String priSchCode = request.getParameter("priSchCode");
		String town = request.getParameter("town");
		
		//已经查询的小学数
		List<AllocateRangeQueryRecord> allocateRangeQueryRecords = allocateRangeQueryRecordRepository.
				queryByMemberIdAndRecordTime(member.getMemberId(), new Date());
		
		//会员等级
		Integer memberGrade = member.getMemberGrade();
		//注册会员限制次数
		Integer limitNumReg = commodity.getLimitNumReg();
		//付费会员限制次数
		Integer limitNumPay = commodity.getLimitNumPay();
		//apiCode
		Integer apiCode = commodity.getApiCode();
		//会员标识号
		String memberId = member.getMemberId();
		
		//当天已经使用的次数
		int currentDayUseNum = 0;
		if (allocateRangeQueryRecords != null) {
			currentDayUseNum = allocateRangeQueryRecords.size();
		}
		
		//当前查询的地址是否已经查过
		List<AllocateRangeQueryRecord> allocateRangeQueryRecords2 = allocateRangeQueryRecordRepository.
				findByMemberId(member.getMemberId());
		boolean ifQuery = this.checkCurrentSchoolCodeIfQuery(allocateRangeQueryRecords2, priSchCode);
		
		//注册会员
		if (memberGrade.intValue() == 1 && limitNumReg != null) {
			//已经超过当天查询的最大上限
			if ( (ifQuery &&  currentDayUseNum > limitNumReg.intValue()) || (!ifQuery &&  currentDayUseNum +1 > limitNumReg.intValue()) ) {
				ret.setMessageTitle("温馨提示");
				ret.setResultDesc("您的限免次数已用完");
				ret.setMessageType(OperaResult.Error);
				
				// 用一个 code 值，让前端知道这个要弹窗已经超过当天查询的最大上限
		        ret.setResultCode("maxLimitNum");
		        
		        // 当前会员可用的学豆数
				Integer canUsedBeans = member.getMemberBeans();
				commodity.setCanUsedBeans(canUsedBeans != null? canUsedBeans.toString() : "0");
				
		        ret.setData(commodity);
			}else{
				MemberCommodityNum memberCommodityNum = memberCommodityNumService.queryByMemberIdAndApiCode(memberId, apiCode);
				//免费查询次数
				Integer freeNum = memberCommodityNum.getFreeNum();

				//用豆交换的次数
				Integer exchangeNum = memberCommodityNum.getBuyExchangeNum();
				//未超过最大查询次数，但免费和用豆换的次数已用完，需要提示用户成为会员或使用学豆
				if (!ifQuery && (currentDayUseNum >= freeNum.intValue()+exchangeNum.intValue() && exchangeNum.intValue() == 0)) {
					ret.setMessageTitle("温馨提示");
					ret.setResultDesc("您的限免次数已用完");
					ret.setMessageType(OperaResult.Error);
					
					// 当前会员可用的学豆数
					Integer canUsedBeans = member.getMemberBeans();
					//是否有足够的都交换一次，查询一个小区对应学校的机会
					int isSatisfyExchangeCondtion = 0;
					//30个豆交换一次免费产品的使用机会（具体在这体现为：30个豆查询一个小学对应的中学
					if (canUsedBeans != null && canUsedBeans.intValue() >= 30) {
						isSatisfyExchangeCondtion = 1;
					}
					commodity.setIsSatisfyExchangeCondtion(isSatisfyExchangeCondtion);
					commodity.setExchangeConditon(30);
					commodity.setCanUsedBeans(canUsedBeans != null? canUsedBeans.toString() : "0");
					
					
					String businessData = town;
					if (StringUtils.isNotEmpty(town)) {
						businessData = businessData + "##" + cacheApplicationService.getTownNameByTown(Integer.parseInt(town));
					}
					businessData = businessData + "##" + priSchCode;
					
					SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(Constant.DICT_ID_10002, priSchCode);
					if (schoolChoice != null) {
						businessData = businessData + "##" + schoolChoice.getSchoolName();
					}
					commodity.setBusinessData(businessData);
					
					// 用一个 code 值，让前端知道这个要弹窗限定次数已满
			        ret.setResultCode("limitNum");
			        ret.setData(commodity);
				}else {
					//未超过最大查询次数，但免费和用豆换的次数未用完，让用户查询
					ret.setMessageType(OperaResult.Success);
			        ret.setResultCode("notLimit");
			        ret.setData(commodity);
				}
			}
		}
		//付费会员
		if (memberGrade.intValue() == 2 && limitNumPay != null) {
		
			if ((ifQuery && currentDayUseNum  > limitNumPay) || (!ifQuery && currentDayUseNum + 1 > limitNumPay) ) {
				ret.setMessageTitle("温馨提示");
				ret.setResultDesc("您的限免次数已用完");
				ret.setMessageType(OperaResult.Error);
				
				// 用一个 code 值，让前端知道这个要弹窗限定次数已满
		        ret.setResultCode("maxLimitNum");
		        // 当前会员可用的学豆数
				Integer canUsedBeans = member.getMemberBeans();
				commodity.setCanUsedBeans(canUsedBeans != null? canUsedBeans.toString() : "0");
				
		        ret.setData(commodity);
			}else{
				//未超过最大查询次数让用户查询
				ret.setMessageType(OperaResult.Success);
		        ret.setResultCode("notLimit");
		        ret.setData(commodity);
			}
		}
		return ret;
	}

	@Override
	public void executeAterAllocateRangeQuerySuccess(String memberId, String schoolCode, String schoolName, Integer serviceBlock, Integer town) {
		
		//记录查询日志
		AllocateRangeQueryLog allocateRangeQueryLog = new AllocateRangeQueryLog();
		allocateRangeQueryLog.setMemberId(memberId);
		allocateRangeQueryLog.setSchoolCode(schoolCode);
		allocateRangeQueryLog.setSchoolName(schoolName);
		allocateRangeQueryLog.setServiceBlock(serviceBlock);
		allocateRangeQueryLog.setTown(town);
		allocateRangeQueryLogRepository.save(allocateRangeQueryLog);
		
		//检查当前居住地是否已经查询过
		List<AllocateRangeQueryRecord> allocateRangeQueryRecords = allocateRangeQueryRecordRepository.
				findByMemberIdAndSchoolCode(memberId, schoolCode);
		if (allocateRangeQueryRecords == null || allocateRangeQueryRecords.size() == 0) {
			//写入一条查询记录，并且将购买或用学豆换的次数减一（大于1的情况下）
			AllocateRangeQueryRecord allocateRangeQueryRecord = new AllocateRangeQueryRecord();
			allocateRangeQueryRecord.setMemberId(memberId);
			allocateRangeQueryRecord.setSchoolCode(schoolCode);
			allocateRangeQueryRecord.setSchoolName(schoolName);
			allocateRangeQueryRecord.setServiceBlock(serviceBlock);
			allocateRangeQueryRecord.setTown(town);
			allocateRangeQueryRecordRepository.saveAndFlush(allocateRangeQueryRecord);
			
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110006);
			if (memberCommodityNum == null) {
				log.warn("派位范围查询成功后，根据会员："+memberId+" apicode:"+Constant.DICT_ID_110006 
						+ " 未查询到每个每个商品的免费次数记录，实际上应该存在一条记录");
			}else {
				// 购买或用学豆换的次数
				Integer buyExchangeNum = memberCommodityNum.getBuyExchangeNum();
				if (buyExchangeNum != null && buyExchangeNum.intValue() > 0) {
					memberCommodityNum.setBuyExchangeNum(buyExchangeNum.intValue() - 1);
					memberCommodityNumRepository.saveAndFlush(memberCommodityNum);
				}
			}
		}
		
	}

	@Override
	public OperaResult exchangeOneQuery(Member member) {
		OperaResult ret = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//交换的apiCode
		resultData.put("apiCode", Constant.DICT_ID_110006+"");
		// 当前会员可用的学豆数
		Integer canUsedBeans = member.getMemberBeans();
		//是否有足够的都交换一次，查询一个小学派位范围的机会
		int isSatisfyExchangeCondtion = 0;
		//30个豆交换一次免费产品的使用机会（具体在这体现为：30个豆查询一个小学对应的中学）
		if (canUsedBeans != null && canUsedBeans.intValue() >= 30) {
			isSatisfyExchangeCondtion = 1;
		}
		//满足交换条件，进行交换
		if (isSatisfyExchangeCondtion == 1) {
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(member.getMemberId(), Constant.DICT_ID_110006);
			if (memberCommodityNum != null) {
				
				memberCommodityNum.setBuyExchangeNum(memberCommodityNum.getBuyExchangeNum().intValue() + 1);
				memberCommodityNumRepository.saveAndFlush(memberCommodityNum);
				
				member.setMemberBeans(canUsedBeans - 30);
				memberRepository.saveAndFlush(member);
				
				ret.setResultCode(OperaResult.Success);
				//交换成功
				resultData.put("is_exchange_buy_success", "1");
				ret.setData(resultData);
				return ret;
			}
		}
		ret.setResultCode(OperaResult.Error);
		//交换失败
		resultData.put("is_exchange_buy_success", "0");
		ret.setData(resultData);
		
		return ret;
	}
	
	
	private boolean checkCurrentSchoolCodeIfQuery(List<AllocateRangeQueryRecord> allocateRangeQueryRecord, String currentSchoolCode) {
		boolean ret = false;
		try
		{
			if (allocateRangeQueryRecord != null && allocateRangeQueryRecord.size() > 0) {
				for(AllocateRangeQueryRecord allocateRangeQuery : allocateRangeQueryRecord) {
					if(allocateRangeQuery.getSchoolCode().equals(currentSchoolCode)) {
						ret = true;
						break;
					}
				}
			}
			
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return ret;
	}

	/**
	 * 设置区名称
	 * @param allocateRangeQueryRecords
	 */
	public void setTownName(List<AllocateRangeQueryRecord> allocateRangeQueryRecords) {
		
		if (allocateRangeQueryRecords != null && allocateRangeQueryRecords.size() > 0) {
			for (AllocateRangeQueryRecord oneRecord : allocateRangeQueryRecords) {
				oneRecord.setTownName(cacheApplicationService.getTownNameByTown(oneRecord.getTown()));
			}
		}
	}
	
}
