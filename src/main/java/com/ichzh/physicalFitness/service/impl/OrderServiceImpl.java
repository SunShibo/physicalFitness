package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.Bills;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.domain.Order;
import com.ichzh.physicalFitness.domain.PayOrderParam;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.CommodityRepository;
import com.ichzh.physicalFitness.repository.GoodsOrderRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service("orderService")
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource(name = "goodsOrderRepository")
    private GoodsOrderRepository goodsOrderRepository;

    @Resource(name = "memberRepository")
    private MemberRepository memberRepository;

    @Resource(name = "commodityRepository")
    private CommodityRepository commodityRepository;

    @Resource(name = "redisDistributedLock")
    private RedisLockService redisLockService;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "weChatService")
    private WeChatService weChatService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "dictService")
    private DictService dictService;

    @Resource(name = "timeTask")
    private TimeTask timeTask;
    
    @Resource(name="commodityLimitNumService")
    private CommodityLimitNumService commodityLimitNumService;

    @Override
    public Commodity getInfoQueryParam(HttpServletRequest request, Map<String, Integer> infoKindMap) {
    	
    	// 获取路由
        String uri = request.getServletPath();
        
        // 获取参数：查询信息所属区、服务模块
        String town = request.getParameter("town");
        String serviceBlock = request.getParameter("serviceBlock");
        if (StringUtils.isEmpty(town) || StringUtils.isEmpty(serviceBlock)) {
        	//不在商品中的API，不用校验town和serviceBlock
        	if (infoKindMap.get(uri) != null) {
        		return null;
        	}
        }
        Commodity commodity = new Commodity();
        if (town != null) {
        	commodity.setTown(Integer.valueOf(town));
        }
        if(serviceBlock != null) {
        	commodity.setServiceBlock(Integer.valueOf(serviceBlock));
        }
        
        commodity.setUri(uri);
        // 获取api查询的信息类型的字典值
        commodity.setApiCode(infoKindMap.get(uri));
        return commodity;
    }

    @Override
    public List<GoodsOrder> queryValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(String memberId, String commodityId, Integer orderStatus, Date validTime) {
        return goodsOrderRepository.selectValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(memberId, commodityId, orderStatus, validTime);
    }
    
    @Override
	public List<GoodsOrder> queryValidGoodsOrderByMemberIdAndCommoCodeAndOrderStatus(String memberId,
			String commoCode, Integer orderStatus, Date validTime) {
		return goodsOrderRepository.selectValidSimilarGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(memberId, commoCode, orderStatus, validTime);
	}

	@Override
    public synchronized List<GoodsOrder> statementAccount(Bills bills, Date begin, Date end) {
        // 根据起始和截至时间，查询订单列表
        Map<String, GoodsOrder> map = this.queryGoodsOrderByBeginTimeAndEndTime(begin, end);
        // 需要更新的订单集合
        List<GoodsOrder> list = new ArrayList<>();
        // 详细对账
        for (Order order : bills.getOrderList()) {
            String orderId = order.getOrderId();
            GoodsOrder goodsOrder = map.get(orderId);
            if (goodsOrder == null) {
                // 本地订单不存在，需要重新查询确认，然后才能追增
                this.queryAndCheckGoodsOrder(bills, order, list);
            } else {
                this.checkGoodsOrder(order, goodsOrder, list);
            }
        }
        int size = list.size();
        bills.setUpdateCount(size);
        bills.setRightCount(bills.getOrderList().size() - size - bills.getNoneCount());
        // 更新到数据库
        if (size == 0) {
            return list;
        }
        return goodsOrderRepository.save(list);
    }

    /**
     * 校验订单状态
     *
     * @param order
     * @param goodsOrder
     * @param list
     */
    private void checkGoodsOrder(Order order, GoodsOrder goodsOrder, List<GoodsOrder> list) {
        // 校验订单状态
        String tradingStatus = order.getTradingStatus();
        if ("SUCCESS".equals(tradingStatus) && !Integer.valueOf(2).equals(goodsOrder.getOrderStatus())) {
            goodsOrder.setOrderStatus(2);
            list.add(goodsOrder);
            return;
        }
        if ("REVOKED".equals(tradingStatus) && !Integer.valueOf(3).equals(goodsOrder.getOrderStatus())) {
            goodsOrder.setOrderStatus(3);
            list.add(goodsOrder);
        }
    }

    /**
     * 当一个订单本地没有，微信服务却有的处理
     *
     * @param order
     * @param list
     */
    private void queryAndCheckGoodsOrder(Bills bills, Order order, List<GoodsOrder> list) {
        GoodsOrder goodsOrder = goodsOrderRepository.findByOrderId(order.getOrderId());
        if (goodsOrder != null) {
            this.checkGoodsOrder(order, goodsOrder, list);
            return;
        }
        // TODO 如果数据库根本没有这个订单，但是微信服务却有，暂时不处理
        log.error("本地没有的订单：" + order.getOrderId());
        bills.setNoneCount(bills.getNoneCount() + 1);
    }

    @Override
    public Map<String, GoodsOrder> queryGoodsOrderByBeginTimeAndEndTime(Date begin, Date end) {
        List<GoodsOrder> list = goodsOrderRepository.selectGoodsOrderByBeginTimeAndEndTime(begin, end);
        // 数据结构化
        Map<String, GoodsOrder> map = new HashMap<>();
        for (GoodsOrder goodsOrder : list) {
            map.put(goodsOrder.getOrderId(), goodsOrder);
        }
        return map;
    }

    @Override
    public String getLockKey(PayOrderParam payOrderParam) {
        OperaResult operaResult = payOrderParam.getOperaResult();
        String commodityId = payOrderParam.getCommodity().getCommodityId();
        Member member = userService.queryNewestMember(payOrderParam.getRequest());
        payOrderParam.setMember(member);
        String lockKey = member.getMemberId();
        payOrderParam.setLockKey(lockKey);
        // 对用户进行加锁
        Integer lock = redisLockService.lock(lockKey, 10, 5, 500);
        if (!Integer.valueOf(1).equals(lock)) {
            operaResult.setResultDesc("下单操作频繁，请稍后重试");
            return null;
        }
        // 查询订单是否已存在
        List<GoodsOrder> goodsOrderList = goodsOrderRepository.findByCommodityIdAndMemberId(commodityId, member.getMemberId());
        if (goodsOrderList.isEmpty()) {
            return lockKey;
        }
        // 检查订单，确保不重复下单
        for (GoodsOrder goodsOrder : goodsOrderList) {
            // 判断是否已购买过该商品
            if (Integer.valueOf(2).equals(goodsOrder.getOrderStatus()) && goodsOrder.getValidTime() != null && goodsOrder.getValidTime().getTime() >= payOrderParam.getDate().getTime()) {
                operaResult.setResultDesc("您已购买过该商品，无需重复购买");
                redisLockService.unlock(lockKey);
                return null;
            }
            // 是否已创建过订单
            boolean isPlaceOrder = Integer.valueOf(1).equals(goodsOrder.getOrderStatus());
            // 该商品已下过单，使用学豆不一致
            if (isPlaceOrder && payOrderParam.getPayBeansNum() != goodsOrder.getPayBeansNum()) {
                operaResult.setResultDesc("使用学豆数量已修改，无法继续支付");
                redisLockService.unlock(lockKey);
                return null;
            }
            // 该商品已下单过，且使用学豆数量一致，则返回 warning 类型，前端使用弹窗确认/取消交互
            if (isPlaceOrder) {
                operaResult.setMessageTitle("是否确认支付？");
                operaResult.setMessageType(OperaResult.Warning);
                operaResult.setResultDesc("点击确定，为您核实订单状态，并支付");
                operaResult.setData(goodsOrder);
                redisLockService.unlock(lockKey);
                return null;
            }
        }
        return lockKey;
    }

    @Override
    public String getConfirmPaymentLockKey(PayOrderParam payOrderParam) {
        OperaResult operaResult = payOrderParam.getOperaResult();
        operaResult.setMessageTitle("下单失败");
        operaResult.setMessageType(OperaResult.Error);
        // 订单id校验
        String orderId = payOrderParam.getGoodsOrder().getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            operaResult.setResultDesc("未识别的订单");
            return null;
        }
        Member member = userService.getCurrentLoginUser(payOrderParam.getRequest());
        payOrderParam.setMember(member);
        // 对用户进行加锁
        String lockKey = member.getMemberId();
        Integer lock = redisLockService.lock(lockKey, 10, 5, 500);
        if (!Integer.valueOf(1).equals(lock)) {
            operaResult.setResultDesc("支付操作频繁，请稍后重试");
            return null;
        }
        payOrderParam.setLockKey(lockKey);
        if (!this.checkOrder(payOrderParam)) {
            // 校验不通过
            return null;
        }
        // 查询订单对应的商品信息
        Commodity commodity = commodityRepository.findByCommodityId(payOrderParam.getGoodsOrder().getCommodityId());
        payOrderParam.setCommodity(commodity);
        return lockKey;
    }

    /**
     * 加锁后，对订单进行校验
     * @param payOrderParam
     * @return
     */
    public boolean checkOrder(PayOrderParam payOrderParam) {
        String orderId = payOrderParam.getGoodsOrder().getOrderId();
        OperaResult operaResult = payOrderParam.getOperaResult();
        if (StringUtils.isEmpty(orderId)) {
            operaResult.setResultDesc("订单不存在");
            return false;
        }
        // 查询订单
        GoodsOrder goodsOrder = goodsOrderRepository.findByOrderIdAndMemberId(orderId, payOrderParam.getMember().getMemberId());
        payOrderParam.setGoodsOrder(goodsOrder);
        if (goodsOrder == null) {
            operaResult.setResultDesc("订单不存在");
            return false;
        }
        if (!Integer.valueOf(1).equals(goodsOrder.getOrderStatus())) {
            operaResult.setResultDesc("订单已支付或已取消，无法支付");
            return false;
        }
        // 查询微信服务上的订单信息
        Map<String, String> wxOrderInfo = weChatService.queryWxOrder(orderId);
        if (wxOrderInfo == null || !"SUCCESS".equals(wxOrderInfo.get("return_code"))) {
            operaResult.setResultDesc("服务器繁忙，请稍后再试");
            return false;
        }
        // 业务处理失败，则改为取消状态
        if (!"SUCCESS".equals(wxOrderInfo.get("result_code"))) {
            this.modifyGoodsOrderByOrderIdAndOrderStatus(orderId, 3);
            operaResult.setResultDesc("订单已取消");
            return false;
        }
        // 交易状态：SUCCESS—支付成功、REFUND—转入退款、NOTPAY—未支付、CLOSED—已关闭、REVOKED—已撤销（刷卡支付）、USERPAYING--用户支付中、PAYERROR--支付失败(其他原因，如银行返回失败)
        String tradeState = wxOrderInfo.get("trade_state");
        if ("SUCCESS".equals(tradeState)) {
            // 支付成功，则订单改为已支付状态
            this.modifyGoodsOrderByOrderIdAndOrderStatus(orderId, 2);
            operaResult.setResultDesc("订单核实：已支付成功");
            operaResult.setMessageType(OperaResult.Warning);
            operaResult.setMessageTitle("订单核实结果");
            return false;
        }
        if ("CLOSED".equals(tradeState) || "REVOKED".equals(tradeState) || "PAYERROR".equals(tradeState)) {
            // 支付未成功，订单状态改为取消状态
            this.modifyGoodsOrderByOrderIdAndOrderStatus(orderId, 3);
            operaResult.setResultDesc("订单已取消功");
            return false;
        }
        // 只有未支付状态才允许统一下单
        if (!"NOTPAY".equals(tradeState)) {
            operaResult.setResultDesc("正在为您核实订单状态，请稍后再试");
            return false;
        }
        // 订单过期无法下单
        Date staleDate = DateUtils.addMinutes(goodsOrder.getOrderTime(), selfConfig.getPayTime());
        long payTime = staleDate.getTime() - payOrderParam.getDate().getTime();
        payOrderParam.setPayTime(payTime);
        if (payTime <= 0) {
            operaResult.setResultDesc("订单支付时间已到期，无法再发起支付");
            // 关闭订单
            timeTask.closeOrder(goodsOrder, 0);
            this.modifyGoodsOrderByOrderIdAndOrderStatus(orderId, 3);
            return false;
        }
        return true;
    }

    /**
     * 创建订单核心方法，需要有事务
     *
     * @param payOrderParam 业务复杂，需要用一个对象来传递参数
     * @return
     */
    @Override
    @Transactional
    public GoodsOrder createGoodsOrder(PayOrderParam payOrderParam) {
        // 检查优惠条件是否符合要求（如：学豆是否够用，学豆是否超出商品价格）
        CommodityKindService commodityKindService = CommodityKindService.getInstance(payOrderParam.getCommodity().getCommodityKind());
        assert commodityKindService != null;
        PayBeanService payBeanService = commodityKindService.checkDiscounts(payOrderParam);
        if (payBeanService == null) {
            return null;
        }
        // 创建订单对象
        GoodsOrder goodsOrder = this.buildGoodsOrder(payOrderParam);
        goodsOrderRepository.save(goodsOrder);
        // 调用微信“统一下单”api进行下单
        Map<String, String> map = weChatService.unifiedOrder(payOrderParam);
        OperaResult operaResult = payOrderParam.getOperaResult();
        // 如果下单不成功，则直接取消订单
        if (!"SUCCESS".equals(map.get("return_code")) || !"SUCCESS".equals(map.get("result_code"))) {
            goodsOrder.setOrderStatus(3);
            goodsOrderRepository.save(goodsOrder);
            operaResult.setResultDesc("下单失败，订单已取消");
            return null;
        }
        // 校验微信签名
        String sign = weChatService.getSign(map);
        if (sign == null || !sign.equals(map.get("sign"))) {
            log.info("下单响应校验，签名不正确，算出的签名为：\n" + sign);
            goodsOrder.setOrderStatus(3);
            goodsOrderRepository.save(goodsOrder);
            operaResult.setResultDesc("无法完成微信下单，订单已取消");
            return null;
        }
        // 预支付交易会话标识不能为空
        if (StringUtils.isEmpty(map.get("prepay_id"))) {
            goodsOrder.setOrderStatus(3);
            goodsOrderRepository.save(goodsOrder);
            operaResult.setResultDesc("无法完成微信下单，订单已取消");
            return null;
        }
        // 下单成功（扣除优惠条件，如学豆）
        commodityKindService.modifyMemberByGoodsOrder(goodsOrder, payOrderParam.getMember());
        payOrderParam.setMap(map);
        return goodsOrder;
    }

    @Override
    public GoodsOrder confirmPaymentOrder(PayOrderParam payOrderParam) {
        String orderId = payOrderParam.getGoodsOrder().getOrderId();
        // 调用微信“统一下单”api进行下单
        Map<String, String> map = weChatService.unifiedOrder(payOrderParam);
        OperaResult operaResult = payOrderParam.getOperaResult();
        // 如果下单响应不成功，则返回null
        if (!"SUCCESS".equals(map.get("return_code"))) {
            operaResult.setResultDesc("下单失败，请重试");
            return null;
        }
        // 如果下单业务失败，则取消订单
        if (!"SUCCESS".equals(map.get("result_code"))) {
            this.modifyGoodsOrderByOrderIdAndOrderStatus(orderId, 3);
            operaResult.setResultDesc("下单失败，订单已取消");
            return null;
        }
        // 校验微信签名
        String sign = weChatService.getSign(map);
        if (sign == null || !sign.equals(map.get("sign"))) {
            log.info("下单响应校验，签名不正确，算出的签名为：\n" + sign);
            operaResult.setResultDesc("无法完成微信下单，请重试");
            return null;
        }
        // 预支付交易会话标识不能为空
        if (StringUtils.isEmpty(map.get("prepay_id"))) {
            operaResult.setResultDesc("无法完成微信下单，请重试");
            return null;
        }
        // 下单成功，设置微信返回的响应数据
        payOrderParam.setMap(map);
        return payOrderParam.getGoodsOrder();
    }

    @Override
    public GoodsOrder buildGoodsOrder(PayOrderParam payOrderParam) {
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setCommodityId(payOrderParam.getCommodity().getCommodityId());
        goodsOrder.setMemberId(payOrderParam.getMember().getMemberId());
        goodsOrder.setOrderTime(payOrderParam.getDate());
        goodsOrder.setOrderPrice(payOrderParam.getCommodity().getPrice());
        goodsOrder.setOrderPayment(1);
        goodsOrder.setOrderStatus(1);
        int payBeansNum = payOrderParam.getPayBeansNum();
        goodsOrder.setPayBeansNum(payBeansNum);
        // 订单金额=商品金额-优惠金额
        goodsOrder.setOrderPriceReality(payOrderParam.getCommodity().getPrice().subtract(payOrderParam.getDiscountAmount()));
        
        //会员
        if (payOrderParam.getCommodity().getCommodityKind().intValue() == Constant.DICT_ID_10901) {
        	// 有效期，暂时是一年
            goodsOrder.setValidTime(DateUtils.addYears(payOrderParam.getDate(), 1));
        }else {
        	goodsOrder.setValidTime(DateUtils.addSeconds(payOrderParam.getDate(), 5));
        }
        
        payOrderParam.setGoodsOrder(goodsOrder);
        return goodsOrder;
    }

    @Override
    @Transactional
    public GoodsOrder modifyGoodsOrderByOrderIdAndOrderStatus(String orderId, Integer orderStatus) {
        GoodsOrder goodsOrder = goodsOrderRepository.findByOrderId(orderId);
        // 微信有通知，本地却无该订单的情况，暂时不处理
        if (goodsOrder == null) {
            log.error("订单的号为：" + orderId + " 的订单，微信有支付通知，本地却没有，不处理");
            return null;
        }
        if (orderStatus.equals(goodsOrder.getOrderStatus())) {
            goodsOrder.setUpdateMessage("订单状态一致，无需处理");
            return goodsOrder;
        }
        // 对用户进行加锁，这里的锁，应与下单时的锁是同一个
        String lockKey = goodsOrder.getMemberId();
        Integer lock = redisLockService.lock(lockKey, 10, 5, 500);
        if (!Integer.valueOf(1).equals(lock)) {
            // 加锁失败，则5分钟后进行对账
            timeTask.queryAndCheckOrder(goodsOrder, 5);
            return goodsOrder;
        }
        // 如果订单状态不一致，则进行修改
        goodsOrder.setOrderStatus(orderStatus);
        GoodsOrder order = goodsOrderRepository.save(goodsOrder);
        
        //支付成功调用，调用支付成功后的统一处理方法
        if (order.getOrderStatus() != null && order.getOrderStatus().intValue() == 2) {
        	commodityLimitNumService.executeAfterBuy(order.getMemberId(), goodsOrder.getOrderId(), goodsOrder.getCommodityId());
        }
        
        // 订单变更状态，则处理学豆或会员等级
        Commodity commodity = commodityRepository.findByCommodityId(goodsOrder.getCommodityId());
        Member member = memberRepository.findByMemberId(goodsOrder.getMemberId());
        CommodityKindService commodityKindService = CommodityKindService.getInstance(commodity.getCommodityKind());
        assert commodityKindService != null;
        commodityKindService.modifyMemberByGoodsOrder(goodsOrder, member);
        // 解锁
        redisLockService.unlock(lockKey);
        if (order == null) {
            log.error("订单的号为：" + orderId + " 的订单，订单支付状态修改失败，应该为：" + orderStatus);
        } else {
            log.info("订单的号为：" + orderId + " 的订单，订单支付状态修改成功，已改为：" + orderStatus);
        }
        goodsOrder.setUpdateMessage("订单支付状态已更新为：" + orderStatus);
        return goodsOrder;
    }

    @Override
    public void queryAndCheckOrder(GoodsOrder goodsOrder) {
        // 查询微信服务的订单信息
        Map<String, String> wxOrderInfo = weChatService.queryWxOrder(goodsOrder.getOrderId());
        if (wxOrderInfo == null || !"SUCCESS".equals(wxOrderInfo.get("return_code"))) {
            // 响应状态不成功，则10分钟后再继续查询
            timeTask.queryAndCheckOrder(goodsOrder, 10);
            return;
        }
        // 业务处理失败，则改为取消状态
        if (!"SUCCESS".equals(wxOrderInfo.get("result_code"))) {
            this.modifyGoodsOrderByOrderIdAndOrderStatus(goodsOrder.getOrderId(), 3);
            return;
        }
        // 交易状态：SUCCESS—支付成功、REFUND—转入退款、NOTPAY—未支付、CLOSED—已关闭、REVOKED—已撤销（刷卡支付）、USERPAYING--用户支付中、PAYERROR--支付失败(其他原因，如银行返回失败)
        String tradeState = wxOrderInfo.get("trade_state");
        if ("SUCCESS".equals(tradeState)) {
            // 支付成功，则订单改为已支付状态
            this.modifyGoodsOrderByOrderIdAndOrderStatus(goodsOrder.getOrderId(), 2);
        } else if ("CLOSED".equals(tradeState) || "REVOKED".equals(tradeState) || "PAYERROR".equals(tradeState)) {
            // 支付未成功，订单状态改为取消状态
            this.modifyGoodsOrderByOrderIdAndOrderStatus(goodsOrder.getOrderId(), 3);
        } else if (System.currentTimeMillis() > goodsOrder.getTimeExpire().getTime()) {
            // 订单已过期，则立即关闭订单
            timeTask.closeOrder(goodsOrder, 0);
        }else if ("NOTPAY".equals(tradeState) || "USERPAYING".equals(tradeState)) {
            // 如果处于待支付或支付中，则1分钟后再查询
            timeTask.queryAndCheckOrder(goodsOrder, 1);
        }
    }

    @Override
    public void closeWxOrder(GoodsOrder goodsOrder) {
        // 发起关单
        Map<String, String> map = weChatService.closeOrder(goodsOrder.getOrderId());
        if (map == null || !"SUCCESS".equals(map.get("return_code"))) {
            // 响应状态不成功，则10分钟后再继续关单
            timeTask.closeOrder(goodsOrder, 10);
            return;
        }
        // 如果成功则关单结束
        if ("SUCCESS".equals(map.get("result_code"))) {
            // 更新订单状态（由于发起关单前，应先取消本地订单，这里不再进行取消操作）
            // this.modifyGoodsOrderByOrderIdAndOrderStatus(goodsOrder.getOrderId(), 3);
            return;
        }
        // 如果订单是已支付造成的关单失败，则修改本地订单状态为已支付
        if ("ORDERPAID".equals(map.get("result_code"))) {
            goodsOrder = this.modifyGoodsOrderByOrderIdAndOrderStatus(goodsOrder.getOrderId(), 2);
            if (goodsOrder != null && !Integer.valueOf(2).equals(goodsOrder.getOrderStatus())) {
                // 如果本地设置状态出错，则10分钟后再试
                timeTask.closeOrder(goodsOrder, 10);
            }
        }
    }

    @Override
    public GoodsOrder queryOrderInfoByOrderId(String orderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findByOrderId(orderId);
        dictService.formatGoodsOrderDictName(goodsOrder);
        // 查询订单下的商品
        Commodity commodity = commodityRepository.findByCommodityId(goodsOrder.getCommodityId());
        goodsOrder.setCommodity(commodity);
        dictService.formatCommodityDictName(commodity);
        return goodsOrder;
    }
}
