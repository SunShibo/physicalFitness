package com.ichzh.physicalFitness.repository.ext.impl;

import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.repository.ext.IGoodsOrderRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.*;

@Slf4j
public class GoodsOrderRepositoryImpl extends BaseRepositoryExtImpl implements IGoodsOrderRepositoryExt {

    @Override
    public List<GoodsOrder> selectValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(String memberId, String commodityId, Integer orderStatus, Date validTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("commodityId", commodityId);
        params.put("orderStatus", orderStatus);
        params.put("validTime", DateFormatUtils.format(validTime, "yyyy-MM-dd HH:mm:ss"));
        ISqlElement sqlElement = this.processSql(params, "GoodsOrderRepositoryImpl.selectValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus");
        return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(GoodsOrder.class));
    }
    
    /**
	  * 根据会员id，cmmo_code以及订单状态查询有效期内的相似订单
	 * @return
	 */
	public List<GoodsOrder> selectValidSimilarGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(String memberId,
			String cmmoCode, Integer orderStatus, Date validTime) {
		
		Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("cmmoCode", cmmoCode);
        params.put("orderStatus", orderStatus);
        params.put("validTime", DateFormatUtils.format(validTime, "yyyy-MM-dd HH:mm:ss"));
        ISqlElement sqlElement = this.processSql(params, "GoodsOrderRepositoryImpl.selectValidSimilarGoodsOrderByMemberIdAndCommodityIdAndOrderStatus");
        return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(GoodsOrder.class));
	}



	@Override
    public List<GoodsOrder> selectGoodsOrderByBeginTimeAndEndTime(Date begin, Date end) {
        Map<String, Object> params = new HashMap<>();
        params.put("begin", DateFormatUtils.format(begin, "yyyy-MM-dd HH:mm:ss"));
        params.put("end", DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss"));
        ISqlElement sqlElement = this.processSql(params, "GoodsOrderRepositoryImpl.selectGoodsOrderByBeginTimeAndEndTime");
        return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(GoodsOrder.class));
    }

    @Override
    public int updateOrderStatusByOrderId(String orderId, Integer orderStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("orderStatus", orderStatus);
        ISqlElement sqlElement = this.processSql(params, "GoodsOrderRepositoryImpl.updateOrderStatusByOrderId");
        return this.jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
    }
}
