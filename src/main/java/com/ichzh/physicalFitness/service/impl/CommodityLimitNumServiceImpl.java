package com.ichzh.physicalFitness.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.CommodityRepository;
import com.ichzh.physicalFitness.service.AllocateRangeQueryRecordService;
import com.ichzh.physicalFitness.service.CommodityLimitNumService;
import com.ichzh.physicalFitness.service.JzdSchoolQueryRecordService;
import com.ichzh.physicalFitness.service.MemberShareService;
import com.ichzh.physicalFitness.service.PrimSchoolExportQueryRecordService;
import com.ichzh.physicalFitness.service.RecruitRangeQueryRecordService;
import com.ichzh.physicalFitness.service.RecruitRangeQueryResultRecordService;
import com.ichzh.physicalFitness.service.SchoolDetailQueryRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("commodityLimitNumService")
public class CommodityLimitNumServiceImpl implements CommodityLimitNumService {

	@Autowired
	JzdSchoolQueryRecordService jzdSchoolQueryRecordService;
	@Autowired
	SchoolDetailQueryRecordService schoolDetailQueryRecordService;
	@Autowired
	CommodityRepository commodityRepository;
	@Autowired
	PrimSchoolExportQueryRecordService primSchoolExportQueryRecordService;
	@Autowired
	AllocateRangeQueryRecordService  allocateRangeQueryRecordService;
	@Autowired
	RecruitRangeQueryRecordService recruitRangeQueryRecordService;
	@Autowired
	MemberShareService memberShareService;
	@Autowired
	RecruitRangeQueryResultRecordService recruitRangeQueryResultRecordService;
	
	/**
	 * 执行商品限次策略入口
	 * @param commodity 商品信息
	 * @param member    会员信息
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request) {
		
		Integer apiCode = commodity.getApiCode();
		if (apiCode == null) {
			// TODO：这里需要补充返回
		}
		//居住地查询学校
		if (Constant.DICT_ID_110005 == apiCode.intValue()) {
			return jzdSchoolQueryRecordService.executeLimitNumStrategy(commodity, member, request);
		}
		//派位范围查询
		if (Constant.DICT_ID_110006 == apiCode.intValue()) {
			return allocateRangeQueryRecordService.executeLimitNumStrategy(commodity, member, request);
		}
		//查询中小学详情
		if (Constant.DICT_ID_110010 == apiCode.intValue()) {
			return schoolDetailQueryRecordService.executeLimitNumStrategy(commodity, member, request);
		}
		//升学规划
		if (Constant.DICT_ID_110013 == apiCode.intValue()) {
			return primSchoolExportQueryRecordService.executeLimitNumStrategy(commodity, member, request);
		}
		//招生范围
		if (Constant.DICT_ID_110008 == apiCode.intValue()) {
			return recruitRangeQueryRecordService.executeLimitNumStrategy(commodity, member, request);
		}
		//招生范围——查询更多地址
		if (Constant.DICT_ID_110014 == apiCode.intValue()) {
			return recruitRangeQueryResultRecordService.executeLimitNumStrategy(commodity, member, request);
		}
		
		return null;
	}
	
	
	/**
	 *商品购买成功后调用该方法.(购买会员成功后，不用调用该方法)
	 * @param memberId        会员标识号.
	 * @param orderId         订单号.
	 * @param commodityId     商品标识号.
	 */
	public OperaResult executeAfterBuy(String memberId, String orderId, String commodityId) {
		
		try
		{
			//缴费成功的商品信息
			Commodity commodity = commodityRepository.findOne(commodityId);
			//商品对应的apiCode
			Integer apiCode = commodity.getApiCode();
			//查询中小学详情
		    if (apiCode != null && Constant.DICT_ID_110010 == apiCode.intValue()) {
		    	return schoolDetailQueryRecordService.buyOneQuery(memberId);
		    }
		    //升学规划
		    if (apiCode != null && Constant.DICT_ID_110013 == apiCode.intValue()) {
		    	return primSchoolExportQueryRecordService.buyOneQuery(memberId);
		    }
		    //如果购买的是会员
		    if (Constant.COMMODITY_MEMBER_ID.equals(commodityId)) {
		    	memberShareService.calculatePayMemberGetBeans(memberId);
		    }
		    
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return null;
	}
	
	

}
