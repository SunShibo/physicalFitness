package com.ichzh.physicalFitness.web.api;

import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.service.CommodityService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/commodity")
public class CommodityApi {

    @Resource(name = "commodityService")
    private CommodityService commodityService;

    /**
     * 根据商品id查询商品信息
     * @param commodityId
     * @return
     */
    @RequestMapping("/getCommodity")
    public Commodity getCommodity(String commodityId) {
        // 根据商品id获取商品信息
        return commodityService.queryCommodityByCommodityId(commodityId);
    }

    /**
     * 根据商品类型，获取商品列表
     * @return
     */
    @RequestMapping("/getCommodityList")
    public List<Commodity> getCommodityList(Integer commodityKind) {
        return commodityService.queryCommodityByCommodityKind(commodityKind);
    }
}
