package com.ichzh.physicalFitness.service.impl;

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
import com.ichzh.physicalFitness.model.PrimSchoolExportQueryLog;
import com.ichzh.physicalFitness.model.PrimSchoolExportQueryRecord;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.model.SchoolDetailQueryRecord;
import com.ichzh.physicalFitness.repository.MemberCommodityNumRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.PrimSchoolExportQueryLogRepository;
import com.ichzh.physicalFitness.repository.PrimSchoolExportQueryRecordRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.MemberCommodityNumService;
import com.ichzh.physicalFitness.service.PrimSchoolExportQueryRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("primSchoolExportQueryRecordService")
public class PrimSchoolExportQueryRecordServiceImpl implements PrimSchoolExportQueryRecordService {

	@Autowired
	PrimSchoolExportQueryRecordRepository primSchoolExportQueryRecordRepository;
	@Autowired
	MemberCommodityNumService memberCommodityNumService;
	@Autowired
	MemberCommodityNumRepository memberCommodityNumRepository;
	@Autowired
	PrimSchoolExportQueryLogRepository primSchoolExportQueryLogRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	/**
	 * 执行升学规划限次策略
	 * @param commodity
	 * @param member
	 * @return  resultCode: 
	 *                      limitNum4paymember    超过最大查询次数，但可以购买或用学豆换查询一个学校详情的权限，需要提示用户购买或使用学豆
	 *                      notLimit    未超过最大查询次数，让用户查询	 * 
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request) {
		
		OperaResult ret = new OperaResult();
		//已经查询的学校数
		List<PrimSchoolExportQueryRecord> primSchoolExportQueryRecords = primSchoolExportQueryRecordRepository.findByMemberId(member.getMemberId());
		
		//查询的学校代码
		String schoolCode = request.getParameter("schoolCode");
		//查询的区
		String town = request.getParameter("town");
		//当前查询的学校是否已经查过
	    boolean ifQuery = this.checkCurrentSchoolCodeIfQuery(primSchoolExportQueryRecords, schoolCode);
		
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
		if (primSchoolExportQueryRecords != null ) {
			toCurrentDayQueryNum = primSchoolExportQueryRecords.size();
		}
		if (memberGrade.intValue() == 2) {
			//已经超过查询的最大学校数
			if (!ifQuery && toCurrentDayQueryNum == freeNum.intValue() + exchangeNum.intValue()) {
				ret.setMessageTitle("温馨提示");
				ret.setResultDesc("您的限免次数已用完");
				ret.setMessageType(OperaResult.Error);
				
				// 当前会员可用的学豆数
				Integer canUsedBeans = member.getMemberBeans();
				//是否有足够的都交换一次，查询查询一个小学的升学规划的机会
				int isSatisfyExchangeCondtion = 0;
				//200个豆查询一个小学的升学规划
				if (canUsedBeans != null && canUsedBeans.intValue() >= 200) {
					isSatisfyExchangeCondtion = 1;
				}
				commodity.setIsSatisfyExchangeCondtion(isSatisfyExchangeCondtion);
				commodity.setBusinessData(town+"##"+schoolCode);
				commodity.setExchangeConditon(200);
				// 用一个 code 值，让前端知道这个要弹窗提示用户购买或使用学豆
		        ret.setResultCode("limitNum4paymember");
		        ret.setData(commodity);
			}else {
				//未超过最大查询次数让用户查询
				ret.setMessageType(OperaResult.Success);
		        ret.setResultCode("notLimit");
		        ret.setData(commodity);
			}
		}else {
			ret.setMessageTitle("温馨提示");
			ret.setResultDesc("只有成为会员才能进行升学规划");
			ret.setMessageType(OperaResult.Error);
			
			ret.setResultCode("limitNum4NoPaymember");
	        //commodity.setBusinessData(chioceId);
	        //将商品设置为会员
	        commodity.setCommodityId("2e188ac18c2845ecb44bda619b3841ae");
	        ret.setData(commodity);
		}
				
		return ret;
	}

	@Override
	public void executeAterSchoolDetailQuerySuccess(String memberId, String schoolCode, Integer schoolType,
			String schoolName) {
		
		
		//记录查询日志
		PrimSchoolExportQueryLog primSchoolExportQueryLog = new PrimSchoolExportQueryLog();
		primSchoolExportQueryLog.setMemberId(memberId);
		primSchoolExportQueryLog.setSchoolCode(schoolCode);
		primSchoolExportQueryLog.setSchoolName(schoolName);
		primSchoolExportQueryLogRepository.save(primSchoolExportQueryLog);
		
		
		//检查当前学校是否已经查询过
		List<PrimSchoolExportQueryRecord> primSchoolExportQueryRecords = primSchoolExportQueryRecordRepository.
				findByMemberIdAndSchoolCode(memberId, schoolCode);
		if (primSchoolExportQueryRecords == null || primSchoolExportQueryRecords.size() == 0) {
			//写入一条查询记录，并且将购买或用学豆换的次数减一（大于1的情况下）
			PrimSchoolExportQueryRecord primSchoolExportQueryRecord = new PrimSchoolExportQueryRecord();
			primSchoolExportQueryRecord.setMemberId(memberId);
			primSchoolExportQueryRecord.setSchoolCode(schoolCode);
			primSchoolExportQueryRecord.setSchoolName(schoolName);
			
			primSchoolExportQueryRecordRepository.saveAndFlush(primSchoolExportQueryRecord);
			
		}
		
	}

	@Override
	public OperaResult exchangeOneQuery(Member member) {
		OperaResult ret = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//交换的apiCode
		resultData.put("apiCode", Constant.DICT_ID_110013+"");
		// 当前会员可用的学豆数
		Integer canUsedBeans = member.getMemberBeans();
		//是否有足够的豆交换一次，查询一个学校详情的机会
		int isSatisfyExchangeCondtion = 0;
		//200个豆交换一次免费产品的使用机会（具体在这体现为：200个豆查询一个小学的升学规划）
		if (canUsedBeans != null && canUsedBeans.intValue() >= 200) {
			isSatisfyExchangeCondtion = 1;
		}
		//满足交换条件，进行交换
		if (isSatisfyExchangeCondtion == 1) {
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(member.getMemberId(), Constant.DICT_ID_110013);
			if (memberCommodityNum != null) {
				
				memberCommodityNum.setBuyExchangeNum(memberCommodityNum.getBuyExchangeNum().intValue() + 1);
				memberCommodityNumRepository.saveAndFlush(memberCommodityNum);
				
				member.setMemberBeans(canUsedBeans - 200);
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

	@Override
	public OperaResult buyOneQuery(String memberId) {
		
		OperaResult ret = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//交换的apiCode
		resultData.put("apiCode", Constant.DICT_ID_110013+"");
				
	    try
	    {
			//查询每个每个商品的免费次数记录
			MemberCommodityNum memberCommodityNum = memberCommodityNumService.
					queryByMemberIdAndApiCode(memberId, Constant.DICT_ID_110013);
			if (memberCommodityNum != null) {
				
				memberCommodityNum.setBuyExchangeNum(memberCommodityNum.getBuyExchangeNum().intValue() + 1);
				memberCommodityNumRepository.saveAndFlush(memberCommodityNum);
				
				ret.setResultCode(OperaResult.Success);
				ret.setResultDesc("已成功购买一次新的升学规划权限！");
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

	private boolean checkCurrentSchoolCodeIfQuery(List<PrimSchoolExportQueryRecord> primSchoolExportQueryRecords, String schoolCode) {
		boolean ret = false;
		try
		{      
			if (primSchoolExportQueryRecords != null && primSchoolExportQueryRecords.size() > 0) {
				for(PrimSchoolExportQueryRecord primSchoolExportQueryRecord : primSchoolExportQueryRecords) {
					if(primSchoolExportQueryRecord.getSchoolCode().equals(schoolCode)) {
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
