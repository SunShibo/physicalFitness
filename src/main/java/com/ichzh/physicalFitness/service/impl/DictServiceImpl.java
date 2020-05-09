package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.SysDict;
import com.ichzh.physicalFitness.model.Town;
import com.ichzh.physicalFitness.repository.SysDictRepository;
import com.ichzh.physicalFitness.repository.TownRepository;
import com.ichzh.physicalFitness.service.DictService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dictService")
public class DictServiceImpl implements DictService {

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "townRepository")
    private TownRepository townRepository;

    @Resource(name = "sysDictRepository")
    private SysDictRepository sysDictRepository;

    @Override
    public Map<String, Integer> queryInfoKindMap() {
        List<SysDict> dictList = this.querySysDictByDictType(selfConfig.getInfoKindType());
        Map<String, Integer> map = new HashMap<>();
        for (SysDict dict : dictList) {
            map.put(dict.getDictCode(), dict.getDictId());
        }
        return map;
    }

    @Override
    public List<SysDict> querySysDictByDictType(Integer dictType) {
        return sysDictRepository.findByDictType(dictType);
    }

    @Override
    public void formatGoodsOrderDictName(GoodsOrder goodsOrder) {
        Integer orderStatus = goodsOrder.getOrderStatus();
        if (Integer.valueOf(1).equals(orderStatus)) {
            goodsOrder.setOrderStatusName("未支付");
            return;
        }
        if (Integer.valueOf(2).equals(orderStatus)) {
            goodsOrder.setOrderStatusName("已支付");
            return;
        }
        if (Integer.valueOf(3).equals(orderStatus)) {
            goodsOrder.setOrderStatusName("已取消");
        }
    }

    @Override
    public void formatCommodityDictName(Commodity commodity) {
        // 服务模块和区格式化字典名称
        Map<Integer, String> townMap = this.queryAllTownMap();
        Map<Integer, String> dictMap = this.querySysDictMapByDictType(100);
        String serviceBlockName = dictMap.get(commodity.getServiceBlock());
        String townName = townMap.get(commodity.getTown());
        commodity.setServiceBlockName(serviceBlockName);
        commodity.setTownName(townName);
    }

    @Override
    public Map<Integer, String> queryAllTownMap() {
        List<Town> townList = townRepository.findAll();
        Map<Integer, String> map = new HashMap<>();
        for (Town town : townList) {
            map.put(town.getTownId(), town.getTownName());
        }
        return map;
    }

    @Override
    public Map<Integer, String> querySysDictMapByDictType(Integer dictType) {
        List<SysDict> dictList = null;
        if (dictType == null) {
             dictList = sysDictRepository.findAll();
        } else {
            dictList = sysDictRepository.findByDictType(dictType);
        }
        Map<Integer, String> map = new HashMap<>();
        for (SysDict sysDict : dictList) {
            map.put(sysDict.getDictId(), sysDict.getDictName());
        }
        return map;
    }
}
