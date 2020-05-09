package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.model.MemberShare;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.MemberShareRepository;
import com.ichzh.physicalFitness.repository.ext.IMemberShareRepositoryExt;
import com.ichzh.physicalFitness.run.TimeTask;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class MemberShareRepositoryImpl extends BaseRepositoryExtImpl implements IMemberShareRepositoryExt{
	
	@Autowired
	MemberShareRepository memberShareRepository;
	@Autowired
	TimeTask timeTask;
	@Autowired
	MemberRepository memberRepository;
	
	public void updateMemberShare(String memberId, String shareUserId) {
		// 查询当前是否有分享记录
		MemberShare memberShare = queryMemberShareByOpenId(memberId);
		if(memberShare == null) {
			// 添加分享记录
			Map<String, Object> params = new HashMap<>();
	        params.put("memberId", memberId);
	        params.put("shareUserId", shareUserId);
			ISqlElement sqlElement = this.processSql(params, "MemberShareRepositoryImpl.updateMemberShare");
	        this.jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
		}
	}
	
	
	@Transactional
	public void updateMemberSharePay(String memberId) {
		// 查询当前是否有分享并且没有购买vip记录
		MemberShare memberShare = queryMemberSharePayByOpenId(memberId);
		if(memberShare != null) {
			
			memberShare.setIsCalculatedPay(1);
			memberShare.setCalcaulatedTimePay(new Date());
			memberShareRepository.saveAndFlush(memberShare);
			
	        // 更新用户学豆
	        updateMemberBeans(memberShare.getMemberId(), Constant.BEANS_BUY_VIP_SUCCESS);
	        
	        // 添加学豆记录
	        updateBeansRecord(memberShare.getMemberId(), Constant.BEANS_BUY_VIP_SUCCESS, 1);
		}
	}
	
	
	@Transactional
	public void updateMemberShareBangdingPhoneNumber(Member member) {
		// 查询是否有没有给过学豆的分享记录
		MemberShare memberShare = memberShareRepository.findByOpenIdAndIsCalculated(member.getMemberId(), 0);
		if(memberShare != null) {
			memberShare.setIsCalculated(1);
			memberShare.setCalculatedTime(new Date());
			memberShareRepository.saveAndFlush(memberShare);
			
	        // 更新用户学豆
	        updateMemberBeans(memberShare.getMemberId(), Constant.BEANS_SHARE_SUCCESS);
	        
	        // 添加学豆记录
	        updateBeansRecord(memberShare.getMemberId(), Constant.BEANS_SHARE_SUCCESS, 1);
	        
	        Member shareMember = memberRepository.findByMemberId(memberShare.getMemberId());
	        //发送学豆变化通知
	        timeTask.asynPushBeansChange(shareMember.getMemberBeans(), shareMember.getMemberWeChat(), Constant.BEANS_SHARE_SUCCESS);
	        
		}
	}
	
	
	private MemberShare queryMemberSharePayByOpenId(String memberId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		
		ISqlElement sqlElement = this.processSql(params, "MemberShareRepositoryImpl.queryMemberSharePayByOpenId.query");
		List<MemberShare> lstMemberShare =  this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), 
				new BeanPropertyRowMapper<>(MemberShare.class));
		
		if(CollectionUtils.isEmpty(lstMemberShare)) {
			return null;
		}
		
		return lstMemberShare.get(0);
	}
	
	
	private MemberShare queryMemberShareByOpenId(String memberId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		
		ISqlElement sqlElement = this.processSql(params, "MemberShareRepositoryImpl.queryMemberShareByOpenId.query");
		List<MemberShare> lstMemberShare =  this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), 
				new BeanPropertyRowMapper<>(MemberShare.class));
		
		if(CollectionUtils.isEmpty(lstMemberShare)) {
			return null;
		}
		
		return lstMemberShare.get(0);
	}
	
	
	private void updateMemberBeans(String memberId, int beansCount) {
		Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("beansCount", beansCount);
		ISqlElement sqlElement = this.processSql(params, "MemberShareRepositoryImpl.updateMemberBeans");
        this.jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
	}
	
	
	private void updateBeansRecord(String memberId, int beansCount, int recordType) {
		Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("recordType", recordType);
        params.put("beansNum", beansCount);
		ISqlElement sqlElement = this.processSql(params, "MemberShareRepositoryImpl.updateBeansRecord");
        this.jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
	}
}
