package com.ichzh.physicalFitness.conf;

import com.ichzh.physicalFitness.domain.AppletCard;
import com.ichzh.physicalFitness.domain.SubscribeMessage;
import com.ichzh.physicalFitness.domain.TemplateMessage;
import com.ichzh.physicalFitness.util.ApplicationContextProviderUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 在需要获取配置文件application.yml中的配置项信息时在此定义
 * 在使用时直接注入bean: selfconfig
 *
 * @author yangjinfei
 */
@Configuration
@ConfigurationProperties(prefix = "selfConfig")
@Data
@Slf4j
public class SelfConfig {

	//是否启动定时任务
	private String ifStartScheduledTask;
	
    // 多节点时，标记不同发服务器ID
    private String machineId;

    // 是否使用沙箱仿真测试系统
    private boolean sandboxnew;

    // 获取沙箱仿真验签秘钥API
    private String signkeyUrl;

    // 小程序 appId
    private String appid;

    // 小程序 appSecret
    private String secret;

    // 小程序 商户平台设置的密钥key
    private String key;

    // 授权类型，此处只需填写 authorization_code
    private String grantType;

    // 微信支付分配的商户号
    private String mchId;

    // 小程序 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
    private String deviceInfo;

    // 名类型，默认为MD5，支持HMAC-SHA256和MD5。
    private String signType;

    // 小程序 交易类型
    private String tradeType;

    // auth.code2Session
    // 登录凭证校验。通过 wx.login 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程。
    private String code2SessionUrl;

    // 获取小程序全局唯一后台接口调用凭据 api
    private String tokenUrl;

    // 统一下单。商户在小程序中先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易后调起支付。
    private String unifiedorderUrl;

    // 下载交易账定api
    private String downloadbillUrl;

    // 查询订单
    private String orderqueryUrl;

    // 关闭订单
    private String closeorderUrl;

    // 微信支付结果通知回调 uri
    private String notifyUrl;

    // 微信申请消息服务器填写的的token
    private String token;

    // 微信申请消息服务器填写的消息加密密钥
    private String encodingAESKey;

    // 微信申请消息服务器时选择的加密方式（1:明文模式、2:兼容模式、3:安全模式）
    private Integer encryMode;

    // 微信申请消息服务器时选择的数据格式
    private String dataType;

    // 发送客服消息给用户 api
    private String customerServiceMessageSendUrl;

    // 公众号发送客服消息api
    private String customserviceKfaccountAddUrl;

    // 客服信息响应结构(好入学\幼升小\小升初)
    private Map<String, String> resCustomer;

    // 公众号APPID
    private String publicAppId;

    // 公众号 appSecret
    private String publicAppSecret;

    // 发送订阅消息
    private String subscribeMessageSendUrl;

    // 小程序模板配置数据
    private Map<String, SubscribeMessage> subscribeMessageMap;

    // 公众号发送订阅消息
    private String templateMessageSendUrl;

    // 公众号模板数据配置
    private Map<String, TemplateMessage> templateMessageMap;

    // 小程序卡片配置
    private Map<String, AppletCard> appletCardMap;

    // 文本推送配置
    private Map<String, String> textMap;

    // 公众号查询用户信息api
    private String userInfoUrl;

    // 短信内容
    private String smsMessage;

    // 短信验证码过期时间(毫秒)
    private long smsTime;

    // 是否真实发送短信
    private boolean enableSms;

    // 绑定付费api的信息类型的字典类型
    private Integer infoKindType;

    // 支付过期时间(分钟)
    private Integer payTime;
    
    //mongodb 数据写入
    private String serverUrl;

    /**
     * 获取是否发送短信配置
     * @return
     */
    public static boolean getIsSms() {
        SelfConfig selfconfig = ApplicationContextProviderUtil.getBean(SelfConfig.class);
        return selfconfig.isEnableSms();
    }
}
