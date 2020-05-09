package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;

/**
 * 商品限次策略服务
 * @author yjf
 *
 */
public interface CommodityLimitNumService {

	/**
	 * 执行商品限次策略入口
	 * @param commodity 商品信息
	 * @param member    会员信息
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request);
	
	/**
	 * 非会员商品购买成功后调用该方法.
	 * @param memberId        会员标识号.
	 * @param orderId         订单号.
	 * @param commodityId     商品标识号.
	 */
	public OperaResult executeAfterBuy(String memberId, String orderId, String commodityId);
}
