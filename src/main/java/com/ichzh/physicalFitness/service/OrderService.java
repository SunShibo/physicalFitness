package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.domain.Bills;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.domain.PayOrderParam;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 获取信息查询参数
     * 包括：信息所属区、服务模块
     * @param request
     * @return
     */
    Commodity getInfoQueryParam(HttpServletRequest request, Map<String, Integer> infoKindMap);

    /**
     * 根据会员id，商品id以及订单状态查询有效期内的订单
     * @param memberId
     * @param commodityId
     * @param orderStatus
     */
    List<GoodsOrder> queryValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(String memberId, String commodityId, Integer orderStatus, Date validTime);
    /**
         * 查询是否买过相似的商品
     * @param memberId
     * @param commoCode
     * @param orderStatus
     * @param validTime
     * @return
     */
    List<GoodsOrder> queryValidGoodsOrderByMemberIdAndCommoCodeAndOrderStatus(String memberId, String commoCode, Integer orderStatus, Date validTime);
    

    /**
     * 订单对账
     * @param bills
     * @return
     */
    List<GoodsOrder> statementAccount(Bills bills, Date begin, Date end);

    /**
     * 根基开始时间和结束时间查询订单列表
     * 开始时间 <= 查询条件 < 结束时间
     * @param begin
     * @param end
     * @return
     */
    Map<String, GoodsOrder> queryGoodsOrderByBeginTimeAndEndTime(Date begin, Date end);

    /**
     * 查询和校验订单
     * 返回 redis 锁
     * @param payOrderParam
     * @return
     */
    String getLockKey(PayOrderParam payOrderParam);

    /**
     * 查询和校验订单
     * 返回 redis 锁
     * @param payOrderParam
     * @return
     */
    String getConfirmPaymentLockKey(PayOrderParam payOrderParam);

    /**
     * 核实订单状态
     * @param payOrderParam
     * @return
     */
    boolean checkOrder(PayOrderParam payOrderParam);

    /**
     * 生成订单
     * @param payOrderParam 业务复杂，需要用一个对象来传递参数
     * @return
     */
    GoodsOrder createGoodsOrder(PayOrderParam payOrderParam);

    /**
     * 确认支付订单
     * @param payOrderParam
     * @return
     */
    GoodsOrder confirmPaymentOrder(PayOrderParam payOrderParam);

    /**
     * 初始化订单对象
     * @param payOrderParam
     * @return
     */
    GoodsOrder buildGoodsOrder(PayOrderParam payOrderParam);

    /**
     * 根据订单id更新订单状态
     * @param orderId
     * @return
     */
    GoodsOrder modifyGoodsOrderByOrderIdAndOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单并对账
     * @param goodsOrder
     */
    void queryAndCheckOrder(GoodsOrder goodsOrder);

    /**
     * 关闭微信服务订单
     * @param goodsOrder
     */
    void closeWxOrder(GoodsOrder goodsOrder);

    /**
     *
     * @param orderId
     * @return
     */
    GoodsOrder queryOrderInfoByOrderId(String orderId);

}
