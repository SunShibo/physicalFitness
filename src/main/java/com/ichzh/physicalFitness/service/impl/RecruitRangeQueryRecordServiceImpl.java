package com.ichzh.physicalFitness.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.MemberCommodityNum;
import com.ichzh.physicalFitness.model.RecruitRangeQueryLog;
import com.ichzh.physicalFitness.model.RecruitRangeQueryRecord;
import com.ichzh.physicalFitness.repository.MemberCommodityNumRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.RecruitRangeQueryLogRepository;
import com.ichzh.physicalFitness.repository.RecruitRangeQueryRecordRepository;
import com.ichzh.physicalFitness.service.MemberCommodityNumService;
import com.ichzh.physicalFitness.service.RecruitRangeQueryRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("recruitRangeQueryRecordService")
public class RecruitRangeQueryRecordServiceImpl implements RecruitRangeQueryRecordService {
	
	@Autowired
	RecruitRangeQueryRecordRepository recruitRangeQueryRecordRepository;
	@Autowired
	MemberCommodityNumService memberCommodityNumService;
    @Autowired
	MemberCommodityNumRepository memberCommodityNumRepository; 
    @Autowired
    RecruitRangeQueryLogRepository recruitRangeQueryLogRepository;
    @Autowired
    MemberRepository memberRepository;
    
	/**
	 * 执行招生范围查询限次策略
	 * @param commodity
	 * @param member
	 * @return  resultCode: maxLimitNum 已经超过最大查询次数（不能买也不能用豆换） ,需要提示用户已达最大查询上限，明天再来
     *                      limitNum    未超过最大查询次数，但免费和用豆换的次数已用完，需要提示用户成为会员或使用学豆
     *                      notLimit    未超过最大查询次数，但免费和用豆换的次数未用完，让用户查询	 * 	 
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request) {
		OperaResult ret = new OperaResult();
		//本次查询的学校名称
		String querySchoolName = request.getParameter("querySchoolName");
		String town = request.getParameter("town");
		
		//已经查询的招生范围
		List<RecruitRangeQueryRecord> recruitRangeQueryRecords = recruitRangeQueryRecordRepository.
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
		if (recruitRangeQueryRecords != null) {
			currentDayUseNum = recruitRangeQueryRecords.size();
		}
		
		//当前查询的地址是否已经查过
		List<RecruitRangeQueryRecord> recruitRangeQueryRecords2 = recruitRangeQueryRecordRepository.
				findByMemberId(member.getMemberId());
		boolean ifQuery = this.checkCurrentSchoolNameIfQuery(recruitRangeQueryRecords2, querySchoolName);
		
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
				if (!ifQuery && (currentDayUseNum >= freeNum.intValue()+exchangeNum.intValue() && exchangeNum.intValue() == 0 )) {
					ret.setMessageTitle("温馨提示");
					ret.setResultDesc("您的限免次数已用完");
					ret.setMessageType(OperaResult.Error);
					
					// 当前会员可用的学豆数
					Integer canUsedBeans = member.getMemberBeans();
					//是否有足够的都交换一次，查询一个小区对应学校的机会
					int isSatisfyExchangeCondtion = 0;
					//30个豆交换一次免费产品的使用机会（具体在这体现为：30个豆查询一个学校的招生范围
					if (canUsedBeans != null && canUsedBeans.intValue() >= 30) {
						isSatisfyExchangeCondtion = 1;
					}
					commodity.setIsSatisfyExchangeCondtion(isSatisfyExchangeCondtion);
					commodity.setExchangeConditon(30);
					commodity.setBusinessData(town+"##"+querySchoolName);
					
					// 用一个 code 值，让前端知道这个要弹窗限定次数已满
			        ret.setResultCode("limitNum");
					commodity.setCanUsedBeans(canUsedBeans != null? canUsedBeans.toString() : "0");
					
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
	public void executeAterRecruitRangeQuerySuccess(String memberId, String schoolCode, String schoolName, Integer serviceBlock, Integer town) {
		
		//记录查询日志
		RecruitRangeQueryLog recruitRangeQueryLog = new RecruitRangeQueryLog();
		recruitRangeQueryLog.setMemberId(memberId);
		recruitRangeQueryLog.setSchoolCode(schoolCode);
		recruitRangeQueryLog.setSchoolName(schoolName);
		recruitRangeQueryLog.setServiceBlock(serviceBlock);
		recruitRangeQueryLog.setTown(town);
		
		recruitRangeQueryLogRepository.save(recruitRangeQueryLog);
		
		
		//检查当前居住地是否已经查询过
		List<RecruitRangeQueryRecord> recruitRangeQueryRecords = recruitRangeQueryRecordRepository.
				findByMemberIdAndSchoolName(memberId, schoolName);
		if (recruitRangeQueryRecords == null || recruitRangeQueryRecords.size() == 0) {
			//写入一条查询记录，并且将购买或用学豆换的次数减一（大于1的情况下）
			RecruitRangeQueryRecord recruitRangeQueryRecord = new RecruitRangeQueryRecord();
			recruitRangeQueryRecord.setMemberId(memberId);
			recruitRangeQueryRecord.setSchoolName(schoolName);
			recruitRangeQueryRecord.setServiceBlock(serviceBlock);
			recruitRangeQueryRecord.setTown(town);
			
			recruitRangeQueryRecordRepository.saveAndFlush(recruitRangeQueryRecord);
			
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110008);
			if (memberCommodityNum == null) {
				log.warn("招生范围查询成功后，根据会员："+memberId+" apicode:"+Constant.DICT_ID_110008
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


	public OperaResult exchangeOneQuery(Member member) {
		OperaResult ret = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//交换的apiCode
		resultData.put("apiCode", Constant.DICT_ID_110008+"");
		// 当前会员可用的学豆数
		Integer canUsedBeans = member.getMemberBeans();
		//是否有足够的都交换一次，查询一个小学派位范围的机会
		int isSatisfyExchangeCondtion = 0;
		//30个豆交换一次免费产品的使用机会（具体在这体现为：30个豆查询一个学校的招生范围）
		if (canUsedBeans != null && canUsedBeans.intValue() >= 30) {
			isSatisfyExchangeCondtion = 1;
		}
		//满足交换条件，进行交换
		if (isSatisfyExchangeCondtion == 1) {
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(member.getMemberId(), Constant.DICT_ID_110008);
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

	private boolean checkCurrentSchoolNameIfQuery(List<RecruitRangeQueryRecord> recruitRangeQueryRecords, String currentSchoolName) {
		boolean ret = false;
		try
		{
			if (recruitRangeQueryRecords != null && recruitRangeQueryRecords.size() > 0) {
				for(RecruitRangeQueryRecord recruitRangeQueryRecord : recruitRangeQueryRecords) {
					if(recruitRangeQueryRecord.getSchoolName().equals(currentSchoolName)) {
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
}
