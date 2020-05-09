package com.ichzh.physicalFitness.web.api;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.domain.PayOrderParam;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
public class OrderApi {

    @Resource(name = "orderService")
    private OrderService orderService;

    @Resource(name = "commodityService")
    private CommodityService commodityService;

    @Resource(name = "weChatService")
    private WeChatService weChatService;

    @Resource(name = "redisDistributedLock")
    private RedisLockService redisLockService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "timeTask")
    private TimeTask timeTask;
    
    @Resource(name = "commodityLimitNumService")
    private CommodityLimitNumService commodityLimitNumService;

    /**
     * 订单下单
     * 生成订单说明
     * 1.微信是一个账号只能在一个手机上同时登录，所以加锁应该对用户加锁，而不是用户+商品；商品不是实体，无需考虑库存问题，只需考虑同一个商品，一个用户不能购买两次
     * 2.当用户双击造成并发请求下单时，应阻塞第2个请求，直到第一个请求发送统一下单api完成后，再释放锁，能更好避免二次发送统一下单，减少出现重复付费的概率
     * 3.业务涉及金额，必须要有事务，减少风险
     * 4.订单下单成功后，需要在支付有效期结束后，进行对账处理
     * 5.下单失败，直接删除订单
     * 6.当响应类型为warning类型时，需要页面端弹出确定取消弹窗，这个交互是为了减少出现重复付费的概率。当检查数据库数据发现要购买的商品，已被订单，且处于待支付状态时，使用warning类型响应。
     * 用户点击确定按钮后，走 “/order/confirmPayment” Api路由进行支付
     * 7.如果下单成功，响应客户端的数据已包含支付所需要的全部参数
     *
     * 响应数据说明：
     * messageTitle 为消息框标题；resultDesc 为描述信息；messageType 为消息类型
     * 1.messageType 为 error 时，表示下单失败；
     * 2.messageType 为 success 时，表示下单成功；data 为小程序发起支付所需要的全部参数
     * 3.messageType 为 warning 时，表示用户之前已成功下过单，需要小程序弹窗，用户确认后，调用 “/order/confirmPayment” api进行支付
     *
     * @param commodity 参数只需商品id和学豆数量
     * @param request
     * @return
     */
    @RequestMapping("/placeOrder")
    public OperaResult placeOrder(Commodity commodity, HttpServletRequest request) {
        OperaResult operaResult = new OperaResult();
        operaResult.setMessageTitle("下单失败");
        operaResult.setMessageType("error");
        // 查询商品信息
        commodity = commodityService.queryCommodity(commodity, operaResult);
        if (commodity == null) {
            return operaResult;
        }
        // 业务复杂，需要用一个对象来传递参数
        PayOrderParam payOrderParam = new PayOrderParam();
        payOrderParam.setCommodity(commodity);
        payOrderParam.setPayBeansNum(commodity.getPayBeansNum());
        payOrderParam.setOperaResult(operaResult);
        payOrderParam.setRequest(request);
        payOrderParam.setDate(new Date());
        // 加锁，返回锁
        String lockKey = orderService.getLockKey(payOrderParam);
        if (lockKey == null) {
            return operaResult;
        }
        // 生成订单
        GoodsOrder goodsOrder = orderService.createGoodsOrder(payOrderParam);
        // 解锁
        redisLockService.unlock(lockKey);
        if (goodsOrder == null) {
            return operaResult;
        }
        // 生成微信客户端支付所需的参数
        String prepayId = payOrderParam.getMap().get("prepay_id");
        Map<String, String> wxPaymentParam = weChatService.getWxPaymentParam(prepayId);
        wxPaymentParam.put("orderId", goodsOrder.getOrderId());
        operaResult.setMessageTitle("下单成功");
        operaResult.setMessageType(OperaResult.Success);
        operaResult.setResultDesc("支付时间：" + selfConfig.getPayTime() + "分钟");
        operaResult.setData(wxPaymentParam);
        // 使用异步，做一个定时任务，进行对账，这里设置1分钟后进行查询对账
        timeTask.queryAndCheckOrder(goodsOrder, 1);
        return operaResult;
    }

    /**
     * 当用户重复点击支付按钮时，客户端弹出确认框，用户点击确认时，走此api进行支付
     *
     * 响应数据说明：
     * messageTitle 为消息框标题；resultDesc 为描述信息；messageType 为消息类型
     * 1.messageType 为 error 时，表示重新下单失败；
     * 2.messageType 为 success 时，表示下单成功；data 为小程序发起支付所需要的全部参数
     *
     * @param goodsOrder 参数只需要订单ID
     * @return
     */
    @RequestMapping("/confirmPayment")
    public OperaResult confirmPayment(GoodsOrder goodsOrder, HttpServletRequest request) {
        OperaResult operaResult = new OperaResult();
        // 业务复杂，需要用一个对象来传递参数
        PayOrderParam payOrderParam = new PayOrderParam();
        payOrderParam.setGoodsOrder(goodsOrder);
        payOrderParam.setOperaResult(operaResult);
        payOrderParam.setRequest(request);
        payOrderParam.setDate(new Date());
        // 获取锁
        String lockKey = orderService.getConfirmPaymentLockKey(payOrderParam);
        if (lockKey == null) {
            return operaResult;
        }
        // 统一下单
        goodsOrder = orderService.confirmPaymentOrder(payOrderParam);
        // 解锁
        redisLockService.unlock(lockKey);
        if (goodsOrder == null) {
            return operaResult;
        }
        // 生成微信客户端支付所需的参数
        String prepayId = payOrderParam.getMap().get("prepay_id");
        Map<String, String> wxPaymentParam = weChatService.getWxPaymentParam(prepayId);
        wxPaymentParam.put("orderId", goodsOrder.getOrderId());
        operaResult.setMessageTitle("下单成功");
        operaResult.setMessageType(OperaResult.Success);
        operaResult.setResultDesc("支付时间剩余：" + (payOrderParam.getPayTime() / 60000) + "分钟");
        operaResult.setData(wxPaymentParam);
        // 使用异步，做一个定时任务，进行对账，这里设置1分钟后进行查询对账
        timeTask.queryAndCheckOrder(goodsOrder, 1);
        return operaResult;
    }

    /**
     * 手动核实订单
     *
     * 响应数据说明：
     * messageTitle 为消息框标题；resultDesc 为描述信息；messageType 为消息类型
     * data 为订单对象；当订单不存在时，为空
     *
     * @return
     */
    @RequestMapping("/verifyOrder")
    public OperaResult verifyOrder(GoodsOrder goodsOrder, HttpServletRequest request) {
        OperaResult operaResult = new OperaResult();
        PayOrderParam payOrderParam = new PayOrderParam();
        payOrderParam.setOperaResult(operaResult);
        payOrderParam.setRequest(request);
        payOrderParam.setDate(new Date());
        payOrderParam.setGoodsOrder(goodsOrder);
        // 获取当前登录的用户
        Member member = userService.getCurrentLoginUser(payOrderParam.getRequest());
        payOrderParam.setMember(member);
        // 核实订单
        orderService.checkOrder(payOrderParam);
        goodsOrder = payOrderParam.getGoodsOrder();

        if (Integer.valueOf(1).equals(goodsOrder.getOrderStatus())) {
            goodsOrder.setOrderStatusName("待支付");
        } else if (Integer.valueOf(2).equals(goodsOrder.getOrderStatus())) {
            goodsOrder.setOrderStatusName("已支付");
        } else if (Integer.valueOf(3).equals(goodsOrder.getOrderStatus())) {
            goodsOrder.setOrderStatusName("已取消");
        }
        // 刷新登录信息
        userService.queryNewestMember(request);
        operaResult.setMessageTitle("订单核实成功");
        operaResult.setMessageType(OperaResult.Success);
        operaResult.setData(goodsOrder);
        return operaResult;
    }

    /**
     * 查询订单信息，包含订单下的商品信息以及字典项有中文名
     * @param orderId
     * @return
     */
    @RequestMapping("/getOrderInfo")
    public GoodsOrder getOrderInfo(String orderId) {
        return orderService.queryOrderInfoByOrderId(orderId);
    }
}
