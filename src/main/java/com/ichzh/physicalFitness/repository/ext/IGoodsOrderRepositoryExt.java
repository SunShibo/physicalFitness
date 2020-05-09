package com.ichzh.physicalFitness.repository.ext;

import java.util.Date;
import java.util.List;

import com.ichzh.physicalFitness.model.GoodsOrder;

public interface IGoodsOrderRepositoryExt {

    /**
     * 根据会员id，商品id以及订单状态查询有效期内的订单
     * @return
     */
    List<GoodsOrder> selectValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(String memberId, String commodityId, Integer orderStatus, Date validTime);

    /**
     * 根基开始时间和结束时间查询订单列表
     * 开始时间 <= 查询条件 < 结束时间
     * @param begin
     * @param end
     * @return
     */
    List<GoodsOrder> selectGoodsOrderByBeginTimeAndEndTime(Date begin, Date end);

    /**
     * 根据订单id更新订单状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    int updateOrderStatusByOrderId(String orderId, Integer orderStatus);
    
    
    /**
         * 根据会员id，cmmoCode以及订单状态查询有效期内的相似订单
     * @return
     */
    List<GoodsOrder> selectValidSimilarGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(String memberId, String cmmoCode, Integer orderStatus, Date validTime);
}
