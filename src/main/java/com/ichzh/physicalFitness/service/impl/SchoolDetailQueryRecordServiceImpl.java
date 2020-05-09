package com.ichzh.physicalFitness.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.JzdSchoolQueryRecord;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.MemberCommodityNum;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.model.SchoolDetailQueryLog;
import com.ichzh.physicalFitness.model.SchoolDetailQueryRecord;
import com.ichzh.physicalFitness.repository.MemberCommodityNumRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.SchoolDetailQueryLogRepository;
import com.ichzh.physicalFitness.repository.SchoolDetailQueryRecordRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.MemberCommodityNumService;
import com.ichzh.physicalFitness.service.SchoolDetailQueryRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("schoolDetailQueryRecordService")
public class SchoolDetailQueryRecordServiceImpl implements SchoolDetailQueryRecordService {

	@Autowired
	SchoolDetailQueryRecordRepository schoolDetailQueryRecordRepository;
	@Autowired
    MemberCommodityNumRepository memberCommodityNumRepository;
	@Autowired
    MemberCommodityNumService memberCommodityNumService;
	@Autowired
	SchoolDetailQueryLogRepository schoolDetailQueryLogRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	/**
	 * 执行根据学校详情限次策略
	 * @param commodity
	 * @param member
	 * @return  resultCode: 
	 *                      limitNum4paymember    超过最大查询次数，但可以购买或用学豆换查询一个学校详情的权限，需要提示用户购买或使用学豆
	 *                      notLimit    未超过最大查询次数，让用户查询
	 *                     
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request) {
		
		
		
		OperaResult ret = new OperaResult();
		
		//已经查询的学校数
		List<SchoolDetailQueryRecord> schoolDetailQueryRecords = schoolDetailQueryRecordRepository.findByMemberId(member.getMemberId());
		
		//查询的学校ID
		String chioceId = request.getParameter("chioceId");
		//当前查询的学校是否已经查过
	    boolean ifQuery = this.checkCurrentChoiceIdIfQuery(schoolDetailQueryRecords, chioceId);
		
		//会员等级
		Integer memberGrade = member.getMemberGrade();
		//apiCode
		Integer apiCode = commodity.getApiCode();
		//会员标识号
		String memberId = member.getMemberId();
		
		MemberCommodityNum memberCommodityNum = memberCommodityNumService.queryByMemberIdAndApiCode(memberId, apiCode);
		//免费查询次数
		Integer freeNum = memberCommodityNum.getFreeNum();
		//用豆交换的次数
		Integer exchangeNum = memberCommodityNum.getBuyExchangeNum();
		
		//到目前为止已经查询的学校个数
		int toCurrentDayQueryNum = 0;
		if (schoolDetailQueryRecords != null ) {
			toCurrentDayQueryNum = schoolDetailQueryRecords.size();
		}
		if (memberGrade.intValue() == 2) {
			//已经超过查询的最大学校数
			if (!ifQuery && toCurrentDayQueryNum == freeNum.intValue() + exchangeNum.intValue()) {
				ret.setMessageTitle("温馨提示");
				ret.setResultDesc("您的限免次数已用完");
				ret.setMessageType(OperaResult.Error);
				
				
				// 当前会员可用的学豆数
				Integer canUsedBeans = member.getMemberBeans();
				//是否有足够的都交换一次，查询一个小区对应学校的机会
				int isSatisfyExchangeCondtion = 0;
				//100个豆交换一次免费产品的使用机会（具体在这体现为：100个豆查询一个小区对应的学校）
				if (canUsedBeans != null && canUsedBeans.intValue() >= 100) {
					isSatisfyExchangeCondtion = 1;
				}
				commodity.setIsSatisfyExchangeCondtion(isSatisfyExchangeCondtion);
				commodity.setExchangeConditon(100);
				// 用一个 code 值，让前端知道这个要弹窗提示用户购买或使用学豆
		        ret.setResultCode("limitNum4paymember");
		        commodity.setBusinessData(chioceId);
		        ret.setData(commodity);
			}else {
				//未超过最大查询次数让用户查询
				ret.setMessageType(OperaResult.Success);
		        ret.setResultCode("notLimit");
		        ret.setData(commodity);
			}
		}else {
			
			ret.setMessageTitle("温馨提示");
			ret.setResultDesc("只有成为会员才能进行到校信息查询");
			ret.setMessageType(OperaResult.Error);
			
			ret.setResultCode("limitNum4NoPaymember");
	        commodity.setBusinessData(chioceId);
	        //将商品设置为会员
	        commodity.setCommodityId("2e188ac18c2845ecb44bda619b3841ae");
	        ret.setData(commodity);
		}
				
		return ret;
	}

	/**
	 * 学校详情查询成功后
	 * @param memberId  会员标识号.
	 * @param schoolCode   学校代码.
	 * @param schoolType 学校类型 1:幼儿园 2:中小学
	 * @param schoolName 学校名称
	 */
	public void executeAterSchoolDetailQuerySuccess(String memberId, String schoolCode, Integer schoolType, String schoolName) {
		
		//记录查询日志
		SchoolDetailQueryLog schoolDetailQueryLog = new SchoolDetailQueryLog();
		schoolDetailQueryLog.setMemberId(memberId);
		schoolDetailQueryLog.setSchoolCode(schoolCode);
		schoolDetailQueryLog.setSchoolName(schoolName);
		schoolDetailQueryLog.setSchoolType(schoolType);
		schoolDetailQueryLogRepository.save(schoolDetailQueryLog);
		
		//检查当前学校是否已经查询过
		List<SchoolDetailQueryRecord> schoolDetailQueryRecords = schoolDetailQueryRecordRepository.
				findByMemberIdAndSchoolCode(memberId, schoolCode);
		if (schoolDetailQueryRecords == null || schoolDetailQueryRecords.size() == 0) {
			//写入一条查询记录，并且将购买或用学豆换的次数减一（大于1的情况下）
			SchoolDetailQueryRecord schoolDetailQueryRecord = new SchoolDetailQueryRecord();
			schoolDetailQueryRecord.setMemberId(memberId);
			schoolDetailQueryRecord.setSchoolCode(schoolCode);
			schoolDetailQueryRecord.setSchoolName(schoolName);
			schoolDetailQueryRecord.setSchoolType(schoolType);
			
			schoolDetailQueryRecordRepository.saveAndFlush(schoolDetailQueryRecord);
			
		}
	}
	
	
	/**
	 * 为当前会员用豆交换一次查询机会
	 * @param member Member.
	 * @return
	 */
	public OperaResult exchangeOneQuery(Member member) {
		
		OperaResult ret = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//交换的apiCode
		resultData.put("apiCode", Constant.DICT_ID_110010+"");
		// 当前会员可用的学豆数
		Integer canUsedBeans = member.getMemberBeans();
		//是否有足够的豆交换一次，查询一个学校详情的机会
		int isSatisfyExchangeCondtion = 0;
		//20个豆交换一次免费产品的使用机会（具体在这体现为：100个豆查询一个学校详情）
		if (canUsedBeans != null && canUsedBeans.intValue() >= 100) {
			isSatisfyExchangeCondtion = 1;
		}
		//满足交换条件，进行交换
		if (isSatisfyExchangeCondtion == 1) {
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(member.getMemberId(), Constant.DICT_ID_110010);
			if (memberCommodityNum != null) {
				
				memberCommodityNum.setBuyExchangeNum(memberCommodityNum.getBuyExchangeNum().intValue() + 1);
				memberCommodityNumRepository.saveAndFlush(memberCommodityNum);
				
				member.setMemberBeans(canUsedBeans - 100);
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

	/**
	 * 当前会员买一次查询机会
	 * @param memberId   会员标识号.
	 * @return
	 */
	public OperaResult buyOneQuery(String memberId) {
		OperaResult ret = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//交换的apiCode
		resultData.put("apiCode", Constant.DICT_ID_110010+"");
				
	    try
	    {
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110010);
			if (memberCommodityNum != null) {
				
				memberCommodityNum.setBuyExchangeNum(memberCommodityNum.getBuyExchangeNum().intValue() + 1);
				memberCommodityNumRepository.saveAndFlush(memberCommodityNum);
				
				ret.setResultCode(OperaResult.Success);
				ret.setResultDesc("已成功购买查询一所学校详情的权限！");
				//交换成功
				resultData.put("is_exchange_buy_success", "1");
				ret.setData(resultData);
				return ret;
			}
	    }catch(Exception ex) {
	    	log.error(ex.getMessage(), ex);
	    }
		
		return ret;
	}
	
	private boolean checkCurrentChoiceIdIfQuery(List<SchoolDetailQueryRecord> schoolDetailQueryRecords, String choiceId) {
		boolean ret = false;
		try
		{
			SchoolChoice schoolChoice = cacheApplicationService.getSchoolChoiceBy(new Integer(choiceId));
			String currentSchoolCode = schoolChoice.getSchoolCode();
			if (schoolDetailQueryRecords != null && schoolDetailQueryRecords.size() > 0) {
				for(SchoolDetailQueryRecord schoolDetailQueryRecord : schoolDetailQueryRecords) {
					if(schoolDetailQueryRecord.getSchoolCode().equals(currentSchoolCode) && schoolDetailQueryRecord.getSchoolType().intValue() == 2) {
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
