package com.ichzh.physicalFitness.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品信息
 */
@Entity(name = "commodity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commodity implements Serializable {

    // API商品信息标识号
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "commodity_id")
    private String commodityId;

    // API所属服务模块
    // 10001 上幼儿园
    //10002 上小学
    //10003 上初中
    //10004 中考中招
    //10005 高考高招
    @Column(name = "service_block")
    private Integer serviceBlock;

    // 所属区
    @Column(name = "town")
    private Integer town;

    // 商品名称
    @Column(name = "cmmo_name")
    private String cmmoName;
    
    // 商品对应功能编码
    @Column(name = "cmmo_code")
    private String cmmoCode;

    // 商品价格
    @Column(name = "price")
    private BigDecimal price;

    // 是否下架
    @Column(name = "is_used")
    private Integer isUsed;

    // 信息类型(废除)
    @Column(name = "info_kind")
    private Integer infoKind;
    
    // 是否对会员免费
    @Column(name = "if_free_member")
    private Integer ifFreeMember;
    
    // 商品类别
    @Column(name = "commodity_kind")
    private Integer commodityKind;

    // API编号
    @Column(name = "api_code")
    private Integer apiCode;
    
    //限制次数(注册会员)
    @Column(name = "limit_num_reg")
    private Integer limitNumReg;
    
    //限制次数(付费会员)
    @Column(name = "limit_num_pay")
    private Integer limitNumPay;
    
    //免费使用次数
    @Column(name = "free_num")
    private Integer freeNum;

    // 服务模块名称
    @Transient
    private String serviceBlockName;

    // 区县名称
    @Transient
    private String townName;
    
    // 信息类型对应的api路由
    @Transient
    private String uri;

    // 购买商品使用的学豆数量
    @Transient
    private Integer payBeansNum;
    
    //可用的学豆数量
    @Transient
    private Integer usedBeanNum;
    
    //是否满足交换条件(0:不满足 1：满足)
    @Transient
    private Integer isSatisfyExchangeCondtion;
    
    //交换条件
    @Transient
    private Integer exchangeConditon;
    
    //供跳转的业务数据：如果查询的是居住地对应学校，该值为 小区名称
    @Transient
    private String businessData;
    
    //当前会员可用的学豆数
    @Transient
    private String canUsedBeans;
    
    
}
