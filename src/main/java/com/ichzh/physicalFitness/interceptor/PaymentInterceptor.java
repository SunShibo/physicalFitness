package com.ichzh.physicalFitness.interceptor;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.service.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 信息是否购买校验拦截器
 */
@Slf4j
@Component
public class PaymentInterceptor implements HandlerInterceptor {

    // 信息类型映射关系
    private final Map<String, Integer> infoKindMap;

    private final ResponseService responseService;

    private final OrderService orderService;

    private final UserService userService;

    private final CommodityService commodityService;
    
    private final CommodityLimitNumService commodityLimitNumService;

    @Autowired
    public PaymentInterceptor(DictService dictService, ResponseService responseService, OrderService orderService, UserService userService,
    		CommodityService commodityService, CommodityLimitNumService commodityLimitNumService) {
        this.responseService = responseService;
        this.orderService = orderService;
        this.userService = userService;
        this.commodityService = commodityService;
        // 查询信息类型字典值
        this.infoKindMap = dictService.queryInfoKindMap();
        this.commodityLimitNumService = commodityLimitNumService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取参数：查询信息所属区、服务模块
        Commodity param = orderService.getInfoQueryParam(request, infoKindMap);
        if (param == null) {
            responseService.write(response, new OperaResult("查询失败", "缺少参数", OperaResult.Error));
            return false;
        }
        // 如果api字典码，则按免费算
        if (param.getApiCode() == null) {
            return true;
        }
        // 获取当前登录用户
        Member member =  userService.queryNewestMember(request);
        // 获取商品信息
        Commodity commodity = commodityService.queryCommodityByTownAndServiceBlockAndApiCode(param.getTown(), param.getServiceBlock(), param.getApiCode());
        if (commodity != null) {
        	 // 是否是付费会员，且在有效期内，且商品对付费会员免费，则可以直接查询
            if (Integer.valueOf(2).equals(member.getMemberGrade()) && member.getExpiryDateEnd() != null && member.getExpiryDateEnd().getTime() >= System.currentTimeMillis() && Integer.valueOf(1).equals(commodity.getIfFreeMember())) {
                
            	OperaResult operaResult = commodityLimitNumService.executeLimitNumStrategy(commodity, member, request);
            	if (operaResult != null && !("notLimit".equals(operaResult.getResultCode()))) {
            		responseService.write(response, operaResult);
            		return false;
            	}
            	
            	return true;
            }
        }
        // 商品是否免费，如果免费，则可以直接查询
        if (commodity == null || commodity.getPrice() == null || BigDecimal.valueOf(0).compareTo(commodity.getPrice()) == 0) {
        	
        	if (commodity == null || commodity.getPrice() == null) {
        		return true;
        	}
        	OperaResult operaResult = commodityLimitNumService.executeLimitNumStrategy(commodity, member, request);
        	if (operaResult != null && !("notLimit".equals(operaResult.getResultCode()))) {
        		responseService.write(response, operaResult);
        		return false;
        	}
            return true;
        }
        // 是否已经购买过商品，如果已购买，且未到期，则可以查询
        List<GoodsOrder> goodsOrders = orderService.queryValidGoodsOrderByMemberIdAndCommodityIdAndOrderStatus(member.getMemberId(), commodity.getCommodityId(), 2, new Date());
        if (!goodsOrders.isEmpty()) {
        	
        	//对已经买的收费商品执行限次策略
        	OperaResult operaResult = commodityLimitNumService.executeLimitNumStrategy(commodity, member, request);
        	if (!("limitNum4paymember".equals(operaResult.getResultCode()))) {
        		responseService.write(response, operaResult);
        		return false;
        	}
            return true;
        }
        
        // 是否已购买过类似的商品（功能编码相同），如果已购买，且未到期，则可以查询
        List<GoodsOrder> goodsOrders2 = orderService.queryValidGoodsOrderByMemberIdAndCommoCodeAndOrderStatus(member.getMemberId(), commodity.getCmmoCode(), 2, new Date());
        if (!goodsOrders2.isEmpty()) {
        	
        	//对已经买的收费商品执行限次策略
        	OperaResult operaResult = commodityLimitNumService.executeLimitNumStrategy(commodity, member, request);
        	if (!("limitNum4paymember".equals(operaResult.getResultCode()))) {
        		responseService.write(response, operaResult);
        		return false;
        	}
        	
            return true;
        }
        
        //查询会员商品
        Commodity memberComm = commodityService.queryCommodityByCommodityId("2e188ac18c2845ecb44bda619b3841ae");
        
        // 需要购买商品才能查询
        OperaResult operaResult = new OperaResult("温馨提示", "目前本服务仅对会员开放，欢迎您成为会员。", OperaResult.Error);
        // 用一个 code 值，让前端知道这个要弹窗购买
        operaResult.setResultCode("buy");
        operaResult.setData(memberComm);
        responseService.write(response, operaResult);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	
    }
}
